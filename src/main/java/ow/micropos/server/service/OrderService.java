package ow.micropos.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.MicroPosException;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.enums.*;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.repository.orders.ChargeEntryRepository;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.target.CustomerRepository;
import ow.micropos.server.repository.target.SeatRepository;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired SeatRepository seatRepo;
    @Autowired CustomerRepository customerRepo;
    @Autowired SalesOrderRepository soRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired ProductEntryRepository prodRepo;
    @Autowired ChargeEntryRepository chargeRepo;
    @Autowired SalesOrderService soService;
    @Autowired PrintService printService;
    @Autowired AuthService authService;

    @Transactional(readOnly = false)
    public long order(Employee employee, SalesOrder currOrder) {

        SalesOrder prevOrder = currOrder.getId() == null ? null : soRepo.findOne(currOrder.getId());
        boolean hasPrev = (prevOrder != null);

        // Validation
        validateOrder(employee, prevOrder, currOrder);

        // Persistence
        soService.saveSalesOrder(currOrder);

        // Printing
        printService.printOrder(currOrder, hasPrev);

        // Processing
        transitionOrder(currOrder);

        // Update Customer Previous Order
        if (currOrder.hasType(SalesOrderType.TAKEOUT) && currOrder.getCustomer() != null) {
            Customer customer = customerRepo.findOne(currOrder.getCustomer().getId());
            customer.setPreviousOrder(currOrder.getSummary());
            customerRepo.save(customer);
        }

        return currOrder.getId();

    }

    /******************************************************************
     *                                                                *
     * State Transitioning Methods
     *
     * Use transitionOrder(SalesOrder). The rest are helper methods.
     *
     * Sales Order
     *      REQUEST_OPEN -> OPEN
     *      REQUEST_CLOSE -> CLOSED
     *      REQUEST_VOID -> VOID
     *
     * Product Entry
     *      REQUEST_SENT -> SENT
     *      REQUEST_HOLD -> HOLD
     *      REQUEST_EDIT -> SENT
     *      REQUEST_VOID -> VOID
     *      REQUEST_HOLD_VOID -> VOID
     *
     * Payment Entry
     *      REQUEST_PAID -> PAID
     *      REQUEST_VOID -> VOID
     *
     * Charge Entry
     *      REQUEST_APPLY -> APPLIED
     *      REQUEST_VOID -> VOID
     *                                                                *
     ******************************************************************/

    private void transitionOrder(SalesOrder order) {

        _transitionSalesOrder(order);

        order.getPaymentEntries()
                .forEach(this::_transitionPaymentEntry);

        order.getProductEntries()
                .forEach(this::_transitionProductEntry);

        order.getChargeEntries()
                .forEach(this::_transitionChargeEntry);

    }

    private void _transitionSalesOrder(SalesOrder item) {

        switch (item.getStatus()) {

            case REQUEST_OPEN:
                item.setStatus(SalesOrderStatus.OPEN);
                soRepo.save(item);
                break;

            case REQUEST_CLOSE:
                item.setStatus(SalesOrderStatus.CLOSED);
                soRepo.save(item);
                break;

            case REQUEST_VOID:
                item.setStatus(SalesOrderStatus.VOID);
                soRepo.save(item);
                break;

            default:
                break;

        }

    }

    private void _transitionProductEntry(ProductEntry item) {

        switch (item.getStatus()) {

            case REQUEST_SENT:
                item.setStatus(ProductEntryStatus.SENT);
                prodRepo.save(item);
                break;

            case REQUEST_HOLD:
                item.setStatus(ProductEntryStatus.HOLD);
                prodRepo.save(item);
                break;

            case REQUEST_EDIT:
                item.setStatus(ProductEntryStatus.SENT);
                prodRepo.save(item);
                break;

            case REQUEST_VOID:
            case REQUEST_HOLD_VOID:
                item.setStatus(ProductEntryStatus.VOID);
                prodRepo.save(item);
                break;

            default:
                break;
        }
    }

    private void _transitionPaymentEntry(PaymentEntry item) {

        switch (item.getStatus()) {

            case REQUEST_PAID:
                item.setStatus(PaymentEntryStatus.PAID);
                payRepo.save(item);
                break;

            case REQUEST_VOID:
                item.setStatus(PaymentEntryStatus.VOID);
                payRepo.save(item);
                break;

            default:
                break;
        }
    }

    private void _transitionChargeEntry(ChargeEntry item) {

        switch (item.getStatus()) {

            case REQUEST_APPLY:
                item.setStatus(ChargeEntryStatus.APPLIED);
                chargeRepo.save(item);
                break;

            case REQUEST_VOID:
                item.setStatus(ChargeEntryStatus.VOID);
                chargeRepo.save(item);
                break;

            default:
                break;
        }

    }

    /******************************************************************
     *                                                                *
     * State and Permission Validation Methods
     *
     * Use validateOrder(Employee, SalesOrder, SalesOrder). The
     * others are helper methods.
     *
     * Only OPEN, REQUEST_OPEN, REQUEST_CLOSE orders can be changed.
     *                                                                *
     ******************************************************************/

    private void validateOrder(
            @NotNull Employee employee,
            @Nullable SalesOrder prevOrder,
            @NotNull SalesOrder currOrder
    ) {
        boolean canChange = currOrder.hasStatus(SalesOrderStatus.OPEN)
                || currOrder.hasStatus(SalesOrderStatus.REQUEST_OPEN)
                || currOrder.hasStatus(SalesOrderStatus.REQUEST_CLOSE);

        _validateSalesOrder(canChange, employee, prevOrder, currOrder);

        currOrder.getProductEntries()
                .forEach(currPE -> {
                    ProductEntry prevPE = (currPE.getId() == null ? null : prodRepo.findOne(currPE.getId()));
                    _validateProductEntry(canChange, employee, prevPE, currPE);
                });

        currOrder.getPaymentEntries()
                .forEach(currPE -> {
                    PaymentEntry prevPE = (currPE.getId() == null ? null : payRepo.findOne(currPE.getId()));
                    _validatePaymentEntry(canChange, employee, prevPE, currPE);
                });

        currOrder.getChargeEntries()
                .forEach(currCE -> {
                    ChargeEntry prevCE = (currCE.getId() == null ? null : chargeRepo.findOne(currCE.getId()));
                    _validateChargeEntry(canChange, employee, prevCE, currCE);
                });
    }

    private void _validateSalesOrder(
            boolean canChange,
            @NotNull Employee employee,
            @Nullable SalesOrder prevOrder,
            @NotNull SalesOrder currOrder
    ) {

        if (prevOrder == null) {

            if (currOrder.hasStatus(SalesOrderStatus.REQUEST_OPEN))
                authService.authorize(employee, Permission.CREATE_SALES_ORDER);
            else if (currOrder.hasStatus(SalesOrderStatus.REQUEST_CLOSE))
                authService.authorize(employee, Permission.CREATE_SALES_ORDER, Permission.CLOSE_SALES_ORDER);
            else
                throw new MicroPosException("Invalid Initial Order Status : " + currOrder.getStatus());

        } else if (!canChange && _hasChanged(prevOrder, currOrder)) {

            throw new MicroPosException("Sales Order can not be modified");

        } else if (currOrder.hasType(SalesOrderType.TAKEOUT)
                && currOrder.getGratuityPercent().compareTo(BigDecimal.ZERO) != 0) {

            throw new MicroPosException("Take Out can not have gratuity");

        } else if (prevOrder.hasStatus(SalesOrderStatus.CLOSED)
                && currOrder.hasStatus(SalesOrderStatus.REQUEST_OPEN)) {

            authService.authorize(employee, Permission.REOPEN_SALES_ORDER);

        } else if (prevOrder.hasStatus(SalesOrderStatus.CLOSED)
                && currOrder.hasStatus(SalesOrderStatus.REQUEST_VOID)) {

            authService.authorize(employee, Permission.VOID_SALES_ORDER);

        } else if (prevOrder.hasStatus(SalesOrderStatus.OPEN)
                && currOrder.hasStatus(SalesOrderStatus.REQUEST_VOID)) {

            authService.authorize(employee, Permission.VOID_SALES_ORDER);

        } else if (prevOrder.hasStatus(SalesOrderStatus.OPEN)
                && currOrder.hasStatus(SalesOrderStatus.REQUEST_CLOSE)) {

            authService.authorize(employee, Permission.CLOSE_SALES_ORDER);

        } else if (prevOrder.getStatus() != currOrder.getStatus()) {

            String transition = prevOrder.getStatus() + " -> " + currOrder.getStatus();
            throw new MicroPosException("Invalid Order Status : " + transition);

        }

    }

    private void _validateProductEntry(
            boolean canChange,
            @NotNull Employee employee,
            @Nullable ProductEntry prevPE,
            @NotNull ProductEntry currPE
    ) {

        if (prevPE == null) {

            if (currPE.hasStatus(ProductEntryStatus.REQUEST_SENT))
                authService.authorize(employee, Permission.CREATE_PRODUCT_ENTRY);
            else if (currPE.hasStatus(ProductEntryStatus.REQUEST_HOLD))
                authService.authorize(employee, Permission.HOLD_PRODUCT_ENTRY);
            else
                throw new MicroPosException("Invalid Initial Product Status : " + currPE.getStatus());

        } else if (!canChange && _hasChanged(prevPE, currPE)) {

            throw new MicroPosException("Product Entry can not be modified");

        } else if (prevPE.hasStatus(ProductEntryStatus.HOLD)
                && currPE.hasStatus(ProductEntryStatus.REQUEST_HOLD)) {

            // Allow requesting hold on already held items, otherwise client will complain when
            // using the hold toggle. This should not have any real effect.

        } else if (prevPE.hasStatus(ProductEntryStatus.HOLD)
                && currPE.hasStatus(ProductEntryStatus.REQUEST_SENT)) {

            authService.authorize(employee, Permission.CREATE_PRODUCT_ENTRY);

        } else if (prevPE.hasStatus(ProductEntryStatus.HOLD)
                && currPE.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID)) {

            authService.authorize(employee, Permission.VOID_PRODUCT_ENTRY);

        } else if (prevPE.hasStatus(ProductEntryStatus.SENT)
                && currPE.hasStatus(ProductEntryStatus.REQUEST_VOID)) {

            authService.authorize(employee, Permission.VOID_PRODUCT_ENTRY);

        } else if (prevPE.hasStatus(ProductEntryStatus.SENT)
                && currPE.hasStatus(ProductEntryStatus.REQUEST_EDIT)) {

            authService.authorize(employee, Permission.EDIT_PRODUCT_ENTRY);

        } else if (prevPE.getStatus() != currPE.getStatus()) {

            String transition = prevPE.getStatus() + " -> " + currPE.getStatus();
            throw new MicroPosException("Invalid Product Status : " + transition);

        }
    }

    private void _validatePaymentEntry(
            boolean canChange,
            @NotNull Employee employee,
            @Nullable PaymentEntry prevPE,
            @NotNull PaymentEntry currPE
    ) {

        if (prevPE == null) {

            if (currPE.hasStatus(PaymentEntryStatus.REQUEST_PAID))
                authService.authorize(employee, Permission.CREATE_PAYMENT_ENTRY);
            else
                throw new MicroPosException("Invalid Initial Payment Status : " + currPE.getStatus());

        } else if (!canChange && _hasChanged(prevPE, currPE)) {

            throw new MicroPosException("Payment Entry can not be modified");

        } else if (prevPE.hasStatus(PaymentEntryStatus.PAID)
                && currPE.hasStatus(PaymentEntryStatus.REQUEST_VOID)) {

            authService.authorize(employee, Permission.VOID_PAYMENT_ENTRY);

        } else if (prevPE.getStatus() != currPE.getStatus()) {

            String transition = prevPE.getStatus() + " -> " + currPE.getStatus();
            throw new MicroPosException("Invalid Payment Status : " + transition);

        }

    }

    private void _validateChargeEntry(
            boolean canChange,
            @NotNull Employee employee,
            @Nullable ChargeEntry prevCE,
            @NotNull ChargeEntry currCE
    ) {

        if (prevCE == null) {

            if (currCE.hasStatus(ChargeEntryStatus.REQUEST_APPLY))
                authService.authorize(employee, Permission.CREATE_CHARGE_ENTRY);
            else
                throw new MicroPosException("Invalid Initial Charge Status : " + currCE.getStatus());

        } else if (!canChange && _hasChanged(prevCE, currCE)) {

            throw new MicroPosException("Charge Entry can not be modified");

        } else if (prevCE.hasStatus(ChargeEntryStatus.APPLIED)
                && currCE.hasStatus(ChargeEntryStatus.REQUEST_VOID)) {

            authService.authorize(employee, Permission.VOID_CHARGE_ENTRY);

        } else if (prevCE.getStatus() != currCE.getStatus()) {

            String transition = prevCE.getStatus() + " -> " + currCE.getStatus();
            throw new MicroPosException("Invalid Charge Status : " + transition);

        }

    }

    private boolean _hasChanged(
            @NotNull SalesOrder prevSO,
            @NotNull SalesOrder currSO
    ) {

        Long[] prevPayIds = prevSO.getPaymentEntries().stream().map(PaymentEntry::getId).sorted().toArray(Long[]::new);
        Long[] prevChargeIds = prevSO.getChargeEntries().stream().map(ChargeEntry::getId).sorted().toArray(Long[]::new);
        Long[] currPayIds = currSO.getPaymentEntries().stream().map(PaymentEntry::getId).sorted().toArray(Long[]::new);
        Long[] currChargeIds = currSO.getChargeEntries().stream().map(ChargeEntry::getId).sorted().toArray(Long[]::new);

        if (prevSO.getType() != currSO.getType()
                || !Objects.equals(prevSO.getId(), currSO.getId())
                || prevSO.getTaxPercent().compareTo(currSO.getTaxPercent()) != 0
                || prevSO.getGratuityPercent().compareTo(currSO.getGratuityPercent()) != 0
                || !Arrays.equals(prevPayIds, currPayIds)
                || !Arrays.equals(prevChargeIds, currChargeIds))
            return true;

        // Ensure all previous order entries are accounted for. VOID orders can be ignored.
        // ProductEntry details are not considered on this pass.
        for (ProductEntry prevPE : prevSO.getProductEntries()) {
            if (prevPE.hasStatus(ProductEntryStatus.VOID))
                continue;

            boolean found = false;
            for (ProductEntry currPE : currSO.getProductEntries()) {
                if (Objects.equals(prevPE.getId(), currPE.getId())) {
                    found = true;
                    break;
                }
            }

            if (!found)
                return true;
        }

        return false;

    }

    private boolean _hasChanged(
            @NotNull ProductEntry prevPE,
            @NotNull ProductEntry currPE
    ) {

        Long[] prevIds = prevPE.getModifiers().stream().map(Modifier::getId).sorted().toArray(Long[]::new);
        Long[] currIds = currPE.getModifiers().stream().map(Modifier::getId).sorted().toArray(Long[]::new);

        return prevPE.getStatus() != currPE.getStatus()
                || !Objects.equals(prevPE.getId(), currPE.getId())
                || prevPE.getQuantity().compareTo(currPE.getQuantity()) != 0
                || !Objects.equals(prevPE.getMenuItem().getId(), currPE.getMenuItem().getId())
                || !Arrays.equals(prevIds, currIds);

    }

    private boolean _hasChanged(
            @NotNull PaymentEntry prevPE,
            @NotNull PaymentEntry currPE
    ) {

        return prevPE.getStatus() != currPE.getStatus()
                || prevPE.getType() != currPE.getType()
                || !Objects.equals(prevPE.getId(), currPE.getId())
                || prevPE.getAmount().compareTo(currPE.getAmount()) != 0;

    }

    private boolean _hasChanged(
            @NotNull ChargeEntry prevCE,
            @NotNull ChargeEntry currCE
    ) {

        return prevCE.getStatus() != currCE.getStatus()
                || !Objects.equals(prevCE.getId(), currCE.getId())
                || !Objects.equals(prevCE.getCharge().getId(), currCE.getCharge().getId());

    }

}
