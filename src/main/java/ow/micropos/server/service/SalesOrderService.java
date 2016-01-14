package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.ObjectViewMapper;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.repository.orders.ChargeEntryRepository;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.target.CustomerRepository;
import ow.micropos.server.repository.target.SeatRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesOrderService {

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

    public void filterVoidProductEntries(SalesOrder salesOrder) {

        List<ProductEntry> previousPEs = salesOrder.getProductEntries();

        List<ProductEntry> filteredPEs = previousPEs
                .stream()
                .filter(pe -> !pe.hasStatus(ProductEntryStatus.VOID))
                .collect(Collectors.toList());

        salesOrder.setProductEntries(filteredPEs);

    }
}
