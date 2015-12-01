package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.exception.NoSalesOrdersException;
import ow.micropos.server.exception.UnpaidOrdersException;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.records.PaymentEntryRecord;
import ow.micropos.server.model.records.ProductEntryRecord;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.records.PaymentEntryRecordRepository;
import ow.micropos.server.repository.records.ProductEntryRecordRepository;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MigrationService {

    @Autowired ProductEntryRepository prodRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired SalesOrderRepository soRepo;

    @Autowired ProductEntryRecordRepository prodrRepo;
    @Autowired PaymentEntryRecordRepository payrRepo;
    @Autowired SalesOrderRecordRepository sorRepo;

    @Transactional
    public int migrateSalesOrders() {

        List<SalesOrder> soList = soRepo.findAll();
        if (soList == null)
            throw new InternalServerErrorException("Error retrieving sales orders.");

        if (soList.isEmpty())
            throw new NoSalesOrdersException("No sales orders to migrate.");

        for (SalesOrder so : soList) {
            if (so.getStatus() == SalesOrderStatus.OPEN)
                throw new UnpaidOrdersException();
        }

        List<SalesOrderRecord> sorList = soList.stream().map(SalesOrderRecord::new).collect(Collectors.toList());

        // Persist Complete Sales Order Records
        for (SalesOrderRecord sor : sorList) {

            sor.setId(null);
            sorRepo.save(sor);

            for (PaymentEntryRecord payr : sor.getPaymentEntryRecords()) {
                payr.setId(null);
                payr.setSalesOrderRecord(sor);
                payrRepo.save(payr);
            }

            for (ProductEntryRecord prodr : sor.getProductEntryRecords()) {
                prodr.setId(null);
                prodr.setSalesOrderRecord(sor);
                prodrRepo.save(prodr);
            }

        }

        // Remove Migrated Sales Orders
        for (SalesOrder so : soList) {
            so.getProductEntries().forEach(prodRepo::delete);
            so.getPaymentEntries().forEach(payRepo::delete);
            soRepo.delete(so);
        }

        // Reset Sales Order Tables
        prodRepo.resetIds();
        payRepo.resetIds();
        soRepo.resetIds();

        return sorList.size();

    }

}
