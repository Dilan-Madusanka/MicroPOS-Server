package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.records.PaymentEntryRecord;
import ow.micropos.server.model.records.ProductEntryRecord;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.model.reports.SimpleReport;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired SalesOrderRecordRepository sorRepo;

    @Transactional
    public SimpleReport generateSimpleReport(@Nonnull Date start, @Nonnull Date end) {

        List<SalesOrderRecord> sorList = sorRepo.findByDateBetween(start, end);

        BigDecimal prodTotal = new BigDecimal(0);
        BigDecimal payTotal = new BigDecimal(0);

        for (SalesOrderRecord sor : sorList) {

            for (ProductEntryRecord prodr : sor.getProductEntryRecords()) {
                prodTotal = prodTotal.add(prodr.getMenuItem().getPrice());
                for (Modifier mod : prodr.getModifiers()) {
                    prodTotal = prodTotal.add(mod.getPrice());
                }
            }

            for (PaymentEntryRecord payr : sor.getPaymentEntryRecords()) {
                payTotal = payTotal.add(payr.getAmount());
            }

        }

        return new SimpleReport(sorList.size(), payTotal, prodTotal);

    }

}
