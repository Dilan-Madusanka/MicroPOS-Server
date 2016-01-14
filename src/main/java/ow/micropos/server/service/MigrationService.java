package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.exception.NoSalesOrdersException;
import ow.micropos.server.exception.UnexpectedOrderRequestException;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.records.ChargeEntryRecord;
import ow.micropos.server.model.records.PaymentEntryRecord;
import ow.micropos.server.model.records.ProductEntryRecord;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.repository.orders.ChargeEntryRepository;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.records.ChargeEntryRecordRepository;
import ow.micropos.server.repository.records.PaymentEntryRecordRepository;
import ow.micropos.server.repository.records.ProductEntryRecordRepository;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MigrationService {

    @Autowired ChargeEntryRepository chRepo;
    @Autowired ProductEntryRepository prodRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired SalesOrderRepository soRepo;

    @Autowired ChargeEntryRecordRepository chrRepo;
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
            if (so.hasStatus(SalesOrderStatus.REQUEST_OPEN)
                    || so.hasStatus(SalesOrderStatus.REQUEST_CLOSE)
                    || so.hasStatus(SalesOrderStatus.REQUEST_VOID))
                throw new UnexpectedOrderRequestException(so.getId());
        }

        List<SalesOrderRecord> sorList = soList.stream().map(SalesOrderRecord::new).collect(Collectors.toList());

        // Persist Complete Sales Order Records
        for (SalesOrderRecord sor : sorList) {

            sor.setId(null);
            sorRepo.save(sor);

            for (ChargeEntryRecord chr : sor.getChargeEntryRecords()) {
                chr.setId(null);
                chr.setSalesOrderRecord(sor);
                chrRepo.save(chr);
            }

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
            so.getChargeEntries().forEach(chRepo::delete);
            so.getProductEntries().forEach(prodRepo::delete);
            so.getPaymentEntries().forEach(payRepo::delete);
            soRepo.delete(so);
        }

        return sorList.size();

    }

    @Transactional
    public boolean resetSalesOrderTables() {
        if (chRepo.count() != 0 || prodRepo.count() != 0 || payRepo.count() != 0 || soRepo.count() != 0)
            return false;

        // Reset Sales Order Tables
        chRepo.resetIds();
        prodRepo.resetIds();
        payRepo.resetIds();
        soRepo.resetIds();
        return true;
    }

}
