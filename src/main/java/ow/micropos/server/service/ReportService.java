package ow.micropos.server.service;

import email.com.gmail.ttsai0509.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.model.enums.*;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.records.ChargeEntryRecord;
import ow.micropos.server.model.records.PaymentEntryRecord;
import ow.micropos.server.model.records.ProductEntryRecord;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.model.reports.ActiveSalesReport;
import ow.micropos.server.model.reports.DaySalesReport;
import ow.micropos.server.model.reports.SimpleReport;
import ow.micropos.server.model.reports.Summary;
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

    @Deprecated
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
    public ActiveSalesReport generateActiveSalesReport() {

        ActiveSalesReport report = new ActiveSalesReport(new Date());

        List<SalesOrder> soList = soRepo.findAll();
        for (SalesOrder so : soList) {

            Summary summary;
            switch (so.getStatus()) {
                case CLOSED:
                    summary = report.closedSummary;
                    break;
                case OPEN:
                    summary = report.openSummary;
                    break;
                case VOID:
                    summary = report.voidSummary;
                    break;
                default:
                    continue;
            }

            switch (so.getType()) {
                case TAKEOUT:
                    summary.takeOutCount++;
                    break;
                case DINEIN:
                    summary.dineInCount++;
                    break;
            }

            summary.productCount += so.getProductEntries()
                    .stream()
                    .filter(per -> !per.hasStatus(ProductEntryStatus.VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
                    .count();

            summary.chargeCount += so.getChargeEntries()
                    .stream()
                    .filter(cer -> !cer.hasStatus(ChargeEntryStatus.VOID)
                            && !cer.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                    .count();

            summary.paymentCount += so.getPaymentEntries()
                    .stream()
                    .filter(per -> !per.hasStatus(PaymentEntryStatus.VOID)
                            && !per.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    .count();

            summary.gratuityCount += so.hasGratuity() ? 1 : 0;

            BigDecimal soProductTotal = salesOrderProductEntryTotal(so).max(ZERO_DOLLARS);
            summary.productTotal = summary.productTotal.add(soProductTotal);

            BigDecimal soChargeTotal = salesOrderChargeTotal(so, soProductTotal);
            summary.chargeTotal = summary.chargeTotal.add(soChargeTotal);

            BigDecimal soSubTotal = soProductTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            summary.subTotal = summary.subTotal.add(soSubTotal);

            BigDecimal soTaxTotal = salesOrderTaxTotal(so, soSubTotal).max(ZERO_DOLLARS);
            summary.taxTotal = summary.taxTotal.add(soTaxTotal);

            BigDecimal soGratuityTotal = salesOrderGratuityTotal(so, soSubTotal).max(ZERO_DOLLARS);
            summary.gratuityTotal = summary.gratuityTotal.add(soGratuityTotal);

            BigDecimal soGrandTotal = soSubTotal.add(soTaxTotal).add(soGratuityTotal).max(ZERO_DOLLARS);
            summary.grandTotal = summary.grandTotal.add(soGrandTotal);

            BigDecimal soPaymentTotal = salesOrderPaymentTotal(so).max(ZERO_DOLLARS);
            summary.paymentTotal = summary.paymentTotal.add(soPaymentTotal);
        }

        return report;
    }

    @Transactional(readOnly = true)
    public DaySalesReport generateDaySalesReport(int year, int month, int day) {

        Date start = DateUtils.startOf(year, month, day);
        Date end = DateUtils.endOf(year, month, day);

        DaySalesReport report = new DaySalesReport(start, end);

        List<SalesOrderRecord> sorList = sorRepo.findByDateBetween(start, end);
        for (SalesOrderRecord so : sorList) {

            Summary summary;
            switch (so.getStatus()) {
                case CLOSED:
                    summary = report.closedSummary;
                    break;
                case OPEN:
                    summary = report.openSummary;
                    break;
                case VOID:
                    summary = report.voidSummary;
                    break;
                default:
                    continue;
            }

            switch (so.getType()) {
                case TAKEOUT:
                    summary.takeOutCount++;
                    break;
                case DINEIN:
                    summary.dineInCount++;
                    break;
            }

            summary.productCount += so.getProductEntryRecords()
                    .stream()
                    .filter(per -> !per.hasStatus(ProductEntryStatus.VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
                    .count();

            summary.chargeCount += so.getChargeEntryRecords()
                    .stream()
                    .filter(cer -> !cer.hasStatus(ChargeEntryStatus.VOID)
                            && !cer.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                    .count();

            summary.paymentCount += so.getPaymentEntryRecords()
                    .stream()
                    .filter(per -> !per.hasStatus(PaymentEntryStatus.VOID)
                            && !per.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    .count();

            summary.gratuityCount += so.hasGratuity() ? 1 : 0;

            BigDecimal soProductTotal = salesOrderRecordProductEntryRecordTotal(so).max(ZERO_DOLLARS).add(summary
                    .productTotal);
            summary.productTotal = summary.productTotal.add(soProductTotal);

            BigDecimal soChargeTotal = salesOrderRecordChargeTotal(so, soProductTotal);
            summary.chargeTotal = summary.chargeTotal.add(soChargeTotal);

            BigDecimal soSubTotal = soProductTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            summary.subTotal = summary.subTotal.add(soSubTotal);

            BigDecimal soTaxTotal = salesOrderRecordTaxTotal(so, soSubTotal).max(ZERO_DOLLARS);
            summary.taxTotal = summary.taxTotal.add(soTaxTotal);

            BigDecimal soGratuityTotal = salesOrderRecordGratuityTotal(so, soSubTotal).max(ZERO_DOLLARS);
            summary.gratuityTotal = summary.gratuityTotal.add(soGratuityTotal);

            BigDecimal soGrandTotal = soSubTotal.add(soTaxTotal).add(soGratuityTotal).max(ZERO_DOLLARS);
            summary.grandTotal = summary.grandTotal.add(soGrandTotal);

            BigDecimal soPaymentTotal = salesOrderRecordPaymentTotal(so).max(ZERO_DOLLARS);
            summary.paymentTotal = summary.paymentTotal.add(soPaymentTotal);
        }

        return report;
    }

    /******************************************************************
     *                                                                *
     * Sales Order Totals
     *                                                                *
     ******************************************************************/

    private BigDecimal salesOrderPaymentTotal(SalesOrder so) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentEntry pe : so.getPaymentEntries()) {
            if (!pe.hasStatus(PaymentEntryStatus.VOID)
                    && !pe.hasStatus(PaymentEntryStatus.REQUEST_VOID))
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
            else if (ce.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                ;// Do nothing
            else if (ce.hasType(ChargeType.FIXED_AMOUNT))
                charge = charge.add(ce.getCharge().getAmount());
            else if (ce.hasType(ChargeType.PERCENTAGE))
                charge = charge.add(ce.getCharge().getAmount().multiply(soProductTotal).setScale(2, BigDecimal
                        .ROUND_HALF_UP));
        return charge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderProductEntryTotal(SalesOrder so) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProductEntry pe : so.getProductEntries())
            total = total.add(productEntryTotal(pe));
        return total.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal productEntryTotal(ProductEntry pe) {
        if (pe.hasStatus(ProductEntryStatus.VOID)
                || pe.hasStatus(ProductEntryStatus.REQUEST_VOID)
                || pe.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
            return ZERO_DOLLARS;

        BigDecimal total = pe.getMenuItem().getPrice();
        for (Modifier m : pe.getModifiers())
            total = total.add(m.getPrice());
        return total.multiply(pe.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).max(ZERO_DOLLARS);
    }

    /******************************************************************
     *                                                                *
     * Sales Order Record Totals
     *                                                                *
     ******************************************************************/

    private BigDecimal salesOrderRecordPaymentTotal(SalesOrderRecord so) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentEntryRecord pe : so.getPaymentEntryRecords()) {
            if (!pe.hasStatus(PaymentEntryStatus.VOID)
                    || !pe.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                total = total.add(pe.getAmount());
        }
        return total.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderRecordGratuityTotal(SalesOrderRecord so, BigDecimal soSubTotal) {
        return so.getGratuityPercent().multiply(soSubTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderRecordTaxTotal(SalesOrderRecord so, BigDecimal soSubTotal) {
        return so.getTaxPercent().multiply(soSubTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderRecordChargeTotal(SalesOrderRecord so, BigDecimal soProductTotal) {
        BigDecimal charge = BigDecimal.ZERO;
        for (ChargeEntryRecord ce : so.getChargeEntryRecords())
            if (ce.hasStatus(ChargeEntryStatus.VOID))
                ;// Do nothing
            else if (ce.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                ;// Do nothing
            else if (ce.hasType(ChargeType.FIXED_AMOUNT))
                charge = charge.add(ce.getCharge().getAmount());
            else if (ce.hasType(ChargeType.PERCENTAGE))
                charge = charge.add(ce.getCharge().getAmount().multiply(soProductTotal).setScale(2, BigDecimal
                        .ROUND_HALF_UP));
        return charge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal salesOrderRecordProductEntryRecordTotal(SalesOrderRecord so) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProductEntryRecord pe : so.getProductEntryRecords())
            total = total.add(productEntryRecordTotal(pe));
        return total.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal productEntryRecordTotal(ProductEntryRecord pe) {
        if (pe.hasStatus(ProductEntryStatus.VOID)
                || pe.hasStatus(ProductEntryStatus.REQUEST_VOID)
                || pe.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
            return ZERO_DOLLARS;

        BigDecimal total = pe.getMenuItem().getPrice();
        for (Modifier m : pe.getModifiers())
            total = total.add(m.getPrice());
        return total.multiply(pe.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).max(ZERO_DOLLARS);
    }

}
