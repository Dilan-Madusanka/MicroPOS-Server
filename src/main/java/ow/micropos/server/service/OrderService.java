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
import java.util.List;
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

    @Transactional(readOnly = false)
    public SalesOrder findSalesOrder(long id) {
        return soRepo.findOne(id);
    }

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
    public long saveSalesOrder(SalesOrder salesOrder) {

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

        return salesOrder.getId();
    }

    @Transactional(readOnly = false)
    public long processOrder(Employee employee, SalesOrder currOrder) {

        SalesOrder prevOrder = currOrder.getId() == null ? null : soRepo.findOne(currOrder.getId());

        // Validation
        validateSalesOrder(employee, prevOrder, currOrder);

        currOrder.getProductEntries()
                .forEach(currPE -> {
                    ProductEntry prevPE = (currPE.getId() == null ? null : prodRepo.findOne(currPE.getId()));
                    validateProductEntry(employee, prevPE, currPE);
                });

        currOrder.getPaymentEntries()
                .forEach(currPE -> {
                    PaymentEntry prevPE = (currPE.getId() == null ? null : payRepo.findOne(currPE.getId()));
                    validatePaymentEntry(employee, prevPE, currPE);
                });

        currOrder.getChargeEntries()
                .forEach(currCE -> {
                    ChargeEntry prevCE = (currCE.getId() == null ? null : chargeRepo.findOne(currCE.getId()));
                    validateChargeEntry(employee, prevCE, currCE);
                });

        // Persistence
        long id = saveSalesOrder(currOrder);

        // Processing
        processSalesOrder(currOrder);

        currOrder.getProductEntries()
                .forEach(this::processProductEntry);

        // Update Customer Previous Order
        if (currOrder.hasType(SalesOrderType.TAKEOUT) && currOrder.getCustomer() != null) {
            Customer customer = customerRepo.findOne(currOrder.getCustomer().getId());
            customer.setPreviousOrder(currOrder.getSummary());
            customerRepo.save(customer);
        }

        return id;

    }

    private void validateSalesOrder(
            @NotNull Employee employee,
            @Nullable SalesOrder prevOrder,
            @NotNull SalesOrder currOrder
    ) {

        if (prevOrder == null) {
            if (currOrder.hasStatus(SalesOrderStatus.REQUEST_OPEN))
                authService.authorize(employee, Permission.CREATE_SALES_ORDER);
            else
                throw new MicroPosException("Invalid Order Status : NULL -> " + currOrder.getStatus());

        } else {
            switch (currOrder.getStatus()) {
                case REQUEST_OPEN:
                    authService.authorize(employee, Permission.REOPEN_SALES_ORDER);
                    break;

                case REQUEST_VOID:
                    authService.authorize(employee, Permission.VOID_SALES_ORDER);
                    break;

                case REQUEST_CLOSE:
                    authService.authorize(employee, Permission.CLOSE_SALES_ORDER);
                    break;

                default:
                    if (currOrder.getStatus() != prevOrder.getStatus())
                        throw new MicroPosException(
                                "Invalid Order Status : " + prevOrder.getStatus() + " -> " + currOrder.getStatus()
                        );
            }
        }

    }

    private void validateProductEntry(
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
                throw new MicroPosException("Invalid Product Status : NULL -> " + currPE.getStatus());

        } else {
            switch (currPE.getStatus()) {
                case REQUEST_SENT:
                    authService.authorize(employee, Permission.REOPEN_PRODUCT_ENTRY);
                    break;

                case REQUEST_EDIT:
                    authService.authorize(employee, Permission.EDIT_PRODUCT_ENTRY);
                    break;

                case REQUEST_HOLD:
                    authService.authorize(employee, Permission.HOLD_PRODUCT_ENTRY);
                    break;

                case REQUEST_VOID:
                    authService.authorize(employee, Permission.VOID_PRODUCT_ENTRY);
                    break;

                default:
                    if (currPE.getStatus() != prevPE.getStatus())
                        throw new MicroPosException(
                                "Invalid Product Status : " + prevPE.getStatus() + " -> " + currPE.getStatus()
                        );
            }
        }

    }

    private void validatePaymentEntry(
            @NotNull Employee employee,
            @Nullable PaymentEntry prevPE,
            @NotNull PaymentEntry currPE
    ) {

        if (prevPE == null) {
            if (currPE.hasStatus(PaymentEntryStatus.REQUEST_PAID))
                authService.authorize(employee, Permission.CREATE_PAYMENT_ENTRY);
            else
                throw new MicroPosException("Invalid Payment Status : NULL -> " + currPE.getStatus());

        } else {
            switch (currPE.getStatus()) {
                case REQUEST_PAID:
                    authService.authorize(employee, Permission.REOPEN_PAYMENT_ENTRY);
                    break;

                case REQUEST_VOID:
                    authService.authorize(employee, Permission.VOID_PAYMENT_ENTRY);
                    break;

                default:
                    if (currPE.getStatus() != prevPE.getStatus())
                        throw new MicroPosException(
                                "Invalid Payment Status : " + prevPE.getStatus() + " -> " + currPE.getStatus()
                        );
            }
        }

    }

    private void validateChargeEntry(
            @NotNull Employee employee,
            @Nullable ChargeEntry prevCE,
            @NotNull ChargeEntry currCE
    ) {

        if (prevCE == null) {
            if (currCE.hasStatus(ChargeEntryStatus.REQUEST_APPLY))
                authService.authorize(employee, Permission.CREATE_CHARGE_ENTRY);
            else
                throw new MicroPosException("Invalid Charge Status : NULL -> " + currCE.getStatus());

        } else {
            switch (currCE.getStatus()) {
                case REQUEST_APPLY:
                    authService.authorize(employee, Permission.REOPEN_CHARGE_ENTRY);
                    break;

                case REQUEST_VOID:
                    authService.authorize(employee, Permission.VOID_CHARGE_ENTRY);
                    break;

                default:
                    if (currCE.getStatus() != prevCE.getStatus())
                        throw new MicroPosException(
                                "Invalid Charge Status : " + prevCE.getStatus() + " -> " + currCE.getStatus()
                        );
            }
        }
    }


    private void processSalesOrder(SalesOrder item) {

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

    private void processProductEntry(ProductEntry item) {

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

}
