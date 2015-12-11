package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.model.enums.*;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.records.PaymentEntryRecord;
import ow.micropos.server.model.records.ProductEntryRecord;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.model.reports.CurrentSalesReport;
import ow.micropos.server.model.reports.SimpleReport;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    @Autowired SalesOrderRecordRepository sorRepo;
    @Autowired SalesOrderRepository soRepo;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public CurrentSalesReport generateCurrentSalesReport() {

        List<SalesOrder> soList = soRepo.findAll();
        CurrentSalesReport report = new CurrentSalesReport();

        report.orderCount = soList.size();

        for (SalesOrder so : soList) {
            BigDecimal soProductTotal = salesOrderProductEntryTotal(so).max(ZERO_DOLLARS);
            BigDecimal soChargeTotal = salesOrderChargeTotal(so, soProductTotal);
            BigDecimal soSubTotal = soProductTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            BigDecimal soTaxTotal = salesOrderTaxTotal(so, soSubTotal).max(ZERO_DOLLARS);
            BigDecimal soGratuityTotal = salesOrderGratuityTotal(so, soSubTotal).max(ZERO_DOLLARS);
            BigDecimal soGrandTotal = soSubTotal.add(soTaxTotal).add(soGratuityTotal).max(ZERO_DOLLARS);
            BigDecimal soPaymentTotal = salesOrderPaymentTotal(so).max(ZERO_DOLLARS);
            BigDecimal soChangeTotal = soPaymentTotal.subtract(soGrandTotal);

            switch (so.getStatus()) {
                case OPEN:
                    report.openCount++;
                    break;
                case CLOSED:
                    report.closedCount++;
                    break;
                case VOID:
                    report.voidCount++;
                    break;
            }

            switch (so.getType()) {
                case TAKEOUT:
                    report.takeOutCount++;
                    break;
                case DINEIN:
                    report.dineInCount++;
                    break;
            }

            so.getProductEntries()
                    .stream()
                    .filter(pe -> pe.hasStatus(ProductEntryStatus.VOID))
                    .forEach(pe -> report.productVoidCount++);

            report.productCount += so.getProductEntries().size();
            report.chargeCount += so.getChargeEntries().size();
            report.paymentCount += so.getPaymentEntries().size();

            report.productTotal = report.productTotal.add(soProductTotal);
            report.chargeTotal = report.chargeTotal.add(soChargeTotal);
            report.subTotal = report.subTotal.add(soSubTotal);
            report.taxTotal = report.taxTotal.add(soTaxTotal);
            report.gratuityTotal = report.gratuityTotal.add(soGratuityTotal);
            report.grandTotal = report.grandTotal.add(soGrandTotal);
            report.paymentTotal = report.paymentTotal.add(soPaymentTotal);
            report.changeTotal = report.changeTotal.add(soChangeTotal);
        }

        return report;
    }

    private BigDecimal salesOrderPaymentTotal(SalesOrder so) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentEntry pe : so.getPaymentEntries()) {
            if (!pe.hasStatus(PaymentEntryStatus.VOID))
                total = total.add(pe.getAmount());
        }
        return total.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderGratuityTotal(SalesOrder so, BigDecimal soSubTotal) {
        return so.getGratuityPercent().multiply(soSubTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderTaxTotal(SalesOrder so, BigDecimal soSubTotal) {
        return so.getTaxPercent().multiply(soSubTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderChargeTotal(SalesOrder so, BigDecimal soProductTotal) {
        BigDecimal charge = BigDecimal.ZERO;
        for (ChargeEntry ce : so.getChargeEntries())
            if (ce.hasStatus(ChargeEntryStatus.VOID))
                ;// Do nothing
            else if (ce.hasType(ChargeType.FIXED_AMOUNT))
                charge = charge.add(ce.getCharge().getAmount());
            else if (ce.hasType(ChargeType.PERCENTAGE))
                charge = charge.add(ce.getCharge().getAmount().multiply(soProductTotal).setScale(2, BigDecimal
                        .ROUND_HALF_UP));
        return charge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderProductEntryTotal(SalesOrder so) {
        if (so.hasStatus(SalesOrderStatus.VOID))
            return ZERO_DOLLARS;

        BigDecimal total = BigDecimal.ZERO;
        for (ProductEntry pe : so.getProductEntries())
            total = total.add(productEntryTotal(pe));
        return total.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal productEntryTotal(ProductEntry pe) {
        if (pe.hasStatus(ProductEntryStatus.VOID))
            return ZERO_DOLLARS;

        BigDecimal total = pe.getMenuItem().getPrice();
        for (Modifier m : pe.getModifiers())
            total = total.add(m.getPrice());
        return total.multiply(pe.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).max(ZERO_DOLLARS);
    }

}
