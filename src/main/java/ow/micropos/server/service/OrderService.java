package ow.micropos.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.ObjectViewMapper;
import ow.micropos.server.exception.MicroPosException;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.enums.*;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.people.Customer;
import ow.micropos.server.model.people.Employee;
import ow.micropos.server.repository.orders.ChargeEntryRepository;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.people.CustomerRepository;
import ow.micropos.server.repository.seating.SeatRepository;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired ObjectViewMapper mapper;
    @Autowired SeatRepository seatRepo;
    @Autowired CustomerRepository customerRepo;
    @Autowired SalesOrderRepository soRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired ProductEntryRepository prodRepo;
    @Autowired ChargeEntryRepository chargeRepo;
    @Autowired PrintService printService;
    @Autowired AuthService authService;

    @Transactional(readOnly = true)
    public List<SalesOrder> findSalesOrders(SalesOrderStatus status, SalesOrderType type) {
        if (status == null) {
            if (type == null)
                return soRepo.findAll();
            else
                return soRepo.findByType(type);
        } else {
            if (type == null)
                return soRepo.findByStatus(status);
            else
                return soRepo.findByStatusAndType(status, type);
        }
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findSalesOrdersBySeat(long id, SalesOrderStatus status) {
        if (status != null)
            return seatRepo.findOne(id)
                    .getSalesOrders()
                    .stream()
                    .filter(so -> so.hasStatus(status))
                    .collect(Collectors.toList());
        else
            return seatRepo.findOne(id).getSalesOrders();
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findSalesOrdersByCustomer(long id, SalesOrderStatus status) {
        if (status != null)
            return customerRepo.findOne(id)
                    .getSalesOrders()
                    .stream()
                    .filter(so -> so.hasStatus(status))
                    .collect(Collectors.toList());
        else
            return customerRepo.findOne(id).getSalesOrders();
    }

    @Transactional(readOnly = false)
    public void saveSalesOrder(SalesOrder salesOrder) {

        soRepo.save(salesOrder);

        for (ChargeEntry charge : salesOrder.getChargeEntries()) {
            charge.setSalesOrder(salesOrder);
            chargeRepo.save(charge);
        }

        for (PaymentEntry pay : salesOrder.getPaymentEntries()) {
            pay.setSalesOrder(salesOrder);
            payRepo.save(pay);
        }

        for (ProductEntry prod : salesOrder.getProductEntries()) {
            prod.setSalesOrder(salesOrder);
            prodRepo.save(prod);
        }

    }

    @Transactional(readOnly = false)
    public long processOrder(Employee employee, SalesOrder currOrder) {

        SalesOrder prevOrder = currOrder.getId() == null ? null : soRepo.findOne(currOrder.getId());

        // Validation
        validateOrder(employee, prevOrder, currOrder);

        // Persistence
        saveSalesOrder(currOrder);

        // Processing
        processOrder(currOrder);

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
     * Processing Methods                                             *
     *                                                                *
     * Use processOrder(SalesOrder). The rest are helper methods.     *
     *                                                                *
     * Prints and performs the actual state transition.               *
     *                                                                *
     ******************************************************************/

    private void processOrder(SalesOrder order) {

        _processSalesOrder(order);

        order.getProductEntries()
                .forEach(this::_processProductEntry);

    }

    private void _processSalesOrder(SalesOrder item) {

        printService.printOrder(item);

        String text = mapper.asString(item, View.SalesOrder.class);

        switch (item.getStatus()) {

            case REQUEST_OPEN:
                log.debug("Opening\t" + text);
                item.setStatus(SalesOrderStatus.OPEN);
                soRepo.save(item);
                break;

            case REQUEST_CLOSE:
                log.debug("Closing\t" + text);
                item.setStatus(SalesOrderStatus.CLOSED);
                soRepo.save(item);
                break;

            case REQUEST_VOID:
                log.debug("Voiding\t" + text);
                item.setStatus(SalesOrderStatus.VOID);
                soRepo.save(item);
                break;

            default:
                log.debug("Skipped\t" + text);
        }

    }

    private void _processProductEntry(ProductEntry item) {

        String text = mapper.asString(item, View.ProductEntry.class);

        switch (item.getStatus()) {

            case REQUEST_SENT:
                log.debug("\tSending\t" + text);
                item.setStatus(ProductEntryStatus.SENT);
                prodRepo.save(item);
                break;

            case REQUEST_HOLD:
                log.debug("\tHolding\t" + text);
                item.setStatus(ProductEntryStatus.HOLD);
                prodRepo.save(item);
                break;

            case REQUEST_EDIT:
                log.debug("\tEditing\t" + text);
                item.setStatus(ProductEntryStatus.SENT);
                prodRepo.save(item);
                break;

            case REQUEST_VOID:
                log.debug("\tVoiding\t" + text);
                item.setStatus(ProductEntryStatus.VOID);
                prodRepo.save(item);
                break;

            default:
                log.debug("\tSkipped\t" + text);
        }
    }

    /******************************************************************
     *                                                                *
     * Validation Methods                                             *
     *                                                                *
     * Use validateOrder(Employee, SalesOrder, SalesOrder). The       *
     * others are helper methods.                                     *
     *                                                                *
     * Checks for valid state transitions and modification            *
     * permissions.                                                   *
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

        } else if (prevOrder.hasStatus(SalesOrderStatus.VOID)
                && currOrder.hasStatus(SalesOrderStatus.REQUEST_OPEN)) {

            authService.authorize(employee, Permission.REOPEN_SALES_ORDER);

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
                && currPE.hasStatus(ProductEntryStatus.REQUEST_SENT)) {

            authService.authorize(employee, Permission.CREATE_PRODUCT_ENTRY);

        } else if (prevPE.hasStatus(ProductEntryStatus.HOLD)
                && currPE.hasStatus(ProductEntryStatus.REQUEST_VOID)) {

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

        List<Long> prevProdIds = prevSO.getProductEntries().stream().map(ProductEntry::getId).collect(Collectors
                .toList());
        List<Long> prevPayIds = prevSO.getPaymentEntries().stream().map(PaymentEntry::getId).collect(Collectors
                .toList());
        List<Long> prevChargeIds = prevSO.getChargeEntries().stream().map(ChargeEntry::getId).collect(Collectors
                .toList());

        List<Long> currProdIds = currSO.getProductEntries().stream().map(ProductEntry::getId).collect(Collectors
                .toList());
        List<Long> currPayIds = currSO.getPaymentEntries().stream().map(PaymentEntry::getId).collect(Collectors
                .toList());
        List<Long> currChargeIds = currSO.getChargeEntries().stream().map(ChargeEntry::getId).collect(Collectors
                .toList());

        return prevSO.getStatus() != currSO.getStatus()
                || prevSO.getType() != currSO.getType()
                || !Objects.equals(prevSO.getId(), currSO.getId())
                || prevSO.getTaxPercent().compareTo(currSO.getTaxPercent()) != 0
                || prevSO.getGratuityPercent().compareTo(currSO.getGratuityPercent()) != 0
                || prevProdIds.size() != currProdIds.size()
                || !prevProdIds.containsAll(currProdIds)
                || !currProdIds.containsAll(prevProdIds)
                || prevPayIds.size() != currPayIds.size()
                || !prevPayIds.containsAll(currPayIds)
                || !currPayIds.containsAll(prevPayIds)
                || prevChargeIds.size() != currChargeIds.size()
                || !prevChargeIds.containsAll(currChargeIds)
                || !currChargeIds.containsAll(prevChargeIds);
    }

    private boolean _hasChanged(
            @NotNull ProductEntry prevPE,
            @NotNull ProductEntry currPE
    ) {

        List<Long> prevIds = prevPE.getModifiers().stream().map(Modifier::getId).collect(Collectors.toList());
        List<Long> currIds = currPE.getModifiers().stream().map(Modifier::getId).collect(Collectors.toList());

        return prevPE.getStatus() != currPE.getStatus()
                || !Objects.equals(prevPE.getId(), currPE.getId())
                || prevPE.getQuantity().compareTo(currPE.getQuantity()) != 0
                || !Objects.equals(prevPE.getMenuItem().getId(), currPE.getMenuItem().getId())
                || prevIds.size() != currIds.size()
                || !prevIds.containsAll(currIds)
                || !currIds.containsAll(prevIds);

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
