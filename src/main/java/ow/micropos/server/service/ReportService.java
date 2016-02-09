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
import ow.micropos.server.model.reports.MonthlySalesReport;
import ow.micropos.server.model.reports.SalesReport;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    @Autowired SalesOrderRecordRepository sorRepo;
    @Autowired SalesOrderRepository soRepo;

    @Transactional(readOnly = true)
    @SuppressWarnings("ConstantConditions")
    public SalesReport generateSalesReport(Date start, Date end, SalesOrderStatus status, SalesOrderType type) {

        if (start != null && end == null) {
            end = DateUtils.endOfDay(start);
            start = DateUtils.startOfDay(start);
        } else if (start == null && end != null) {
            start = DateUtils.startOfDay(end);
            end = DateUtils.endOfDay(end);
        }

        SalesReport report = new SalesReport(start, end, status, type);

        if (start == null && end == null && status == null && type == null) {
            summarizeSalesOrders(report, soRepo.findAll());

        } else if (start == null && end == null && status == null && type != null) {
            summarizeSalesOrders(report, soRepo.findByType(type));

        } else if (start == null && end == null && status != null && type == null) {
            summarizeSalesOrders(report, soRepo.findByStatus(status));

        } else if (start == null && end == null && status != null && type != null) {
            summarizeSalesOrders(report, soRepo.findByStatusAndType(status, type));

        } else if (start != null && end != null && status == null && type == null) {
            List<SalesOrderRecord> sors = sorRepo.findByDateBetween(start, end);

            summarizeSalesOrderRecords(report, sors);

        } else if (start != null && end != null && status == null && type != null) {
            List<SalesOrderRecord> sors = sorRepo
                    .findByDateBetween(start, end)
                    .stream()
                    .filter(sor -> sor.hasType(type))
                    .collect(Collectors.toList());

            summarizeSalesOrderRecords(report, sors);

        } else if (start != null && end != null && status != null && type == null) {
            List<SalesOrderRecord> sors = sorRepo
                    .findByDateBetween(start, end)
                    .stream()
                    .filter(sor -> sor.hasStatus(status))
                    .collect(Collectors.toList());

            summarizeSalesOrderRecords(report, sors);

        } else if (start != null && end != null && status != null && type != null) {
            List<SalesOrderRecord> sors = sorRepo
                    .findByDateBetween(start, end)
                    .stream()
                    .filter(sor -> sor.hasType(type) && sor.hasStatus(status))
                    .collect(Collectors.toList());

            summarizeSalesOrderRecords(report, sors);

        }

        return report;

    }

    @Transactional(readOnly = true)
    public MonthlySalesReport generateMonthlySalesReport(Date monthOf) {

        MonthlySalesReport report = new MonthlySalesReport(monthOf);
        Date start = DateUtils.startOfMonth(monthOf);
        Date end = DateUtils.endOfMonth(monthOf);

        Calendar dayStart = Calendar.getInstance();
        dayStart.setTime(start);

        Calendar dayEnd = Calendar.getInstance();
        dayEnd.setTime(DateUtils.endOfDay(dayStart.getTime()));

        while (dayStart.getTime().getTime() < end.getTime()) {
            List<SalesOrderRecord> daySOR = sorRepo
                    .findByDateBetween(dayStart.getTime(), dayEnd.getTime())
                    .stream()
                    .filter(sor -> sor.hasStatus(SalesOrderStatus.CLOSED))
                    .collect(Collectors.toList());

            BigDecimal daySales = ZERO_DOLLARS;
            BigDecimal dayTax = ZERO_DOLLARS;

            for (SalesOrderRecord so : daySOR) {
                BigDecimal soProductTotal = salesOrderRecordProductEntryRecordTotal(so).max(ZERO_DOLLARS);
                BigDecimal soChargeTotal = salesOrderRecordChargeTotal(so, soProductTotal);
                BigDecimal soSubTotal = soProductTotal.add(soChargeTotal).max(ZERO_DOLLARS);
                BigDecimal soTaxTotal = salesOrderRecordTaxTotal(so, soSubTotal).max(ZERO_DOLLARS);
                BigDecimal soGratuityTotal = salesOrderRecordGratuityTotal(so, soSubTotal).max(ZERO_DOLLARS);
                BigDecimal soGrandTotal = soSubTotal.add(soTaxTotal).add(soGratuityTotal).max(ZERO_DOLLARS);
                BigDecimal soNetSales = soGrandTotal.subtract(soGratuityTotal).max(ZERO_DOLLARS);

                daySales = daySales.add(soNetSales).max(ZERO_DOLLARS);
                dayTax = dayTax.add(soTaxTotal).max(ZERO_DOLLARS);
            }

            report.dailySales.add(daySales);
            report.dailyTax.add(dayTax);
            report.netSales = report.netSales.add(daySales);
            report.netTax = report.netTax.add(dayTax);

            dayStart.add(Calendar.DATE, 1);
            dayEnd.add(Calendar.DATE, 1);
        }

        return report;
    }

    /******************************************************************
     *                                                                *
     * Sales Order Totals
     *                                                                *
     ******************************************************************/

    private SalesReport summarizeSalesOrders(SalesReport report, List<SalesOrder> sos) {

        report.orderCount = sos.size();

        for (SalesOrder so : sos) {

            report.productCount += so.getProductEntries()
                    .stream()
                    .filter(per -> !per.hasStatus(ProductEntryStatus.VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
                    .count();

            report.chargeCount += so.getChargeEntries()
                    .stream()
                    .filter(cer -> !cer.hasStatus(ChargeEntryStatus.VOID)
                            && !cer.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                    .count();

            report.paymentCount += so.getPaymentEntries()
                    .stream()
                    .filter(per -> !per.hasStatus(PaymentEntryStatus.VOID)
                            && !per.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    .count();

            for (PaymentEntry pe : so.getPaymentEntries()) {
                if (pe.hasStatus(PaymentEntryStatus.VOID) || pe.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    continue;

                switch (pe.getType()) {
                    case CASH:
                        report.cashCount++;
                        report.cashTotal = report.cashTotal.add(pe.getAmount());
                        break;
                    case CREDIT:
                        report.creditCount++;
                        report.creditTotal = report.creditTotal.add(pe.getAmount());
                        break;
                    case CHECK:
                        report.checkCount++;
                        report.checkTotal = report.checkTotal.add(pe.getAmount());
                        break;
                    case GIFTCARD:
                        report.giftcardCount++;
                        report.giftcardTotal = report.giftcardTotal.add(pe.getAmount());
                        break;
                }
            }

            report.gratuityCount += so.hasGratuity() ? 1 : 0;

            BigDecimal soProductTotal = salesOrderProductEntryTotal(so).max(ZERO_DOLLARS);
            report.productTotal = report.productTotal.add(soProductTotal);

            BigDecimal soChargeTotal = salesOrderChargeTotal(so, soProductTotal);
            report.chargeTotal = report.chargeTotal.add(soChargeTotal);

            BigDecimal soSubTotal = soProductTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            report.subTotal = report.subTotal.add(soSubTotal);

            BigDecimal soTaxTotal = salesOrderTaxTotal(so, soSubTotal).max(ZERO_DOLLARS);
            report.taxTotal = report.taxTotal.add(soTaxTotal);

            BigDecimal soGratuityTotal = salesOrderGratuityTotal(so, soSubTotal).max(ZERO_DOLLARS);
            report.gratuityTotal = report.gratuityTotal.add(soGratuityTotal);

            BigDecimal soGrandTotal = soSubTotal.add(soTaxTotal).add(soGratuityTotal).max(ZERO_DOLLARS);
            report.grandTotal = report.grandTotal.add(soGrandTotal);

            BigDecimal soPaymentTotal = salesOrderPaymentTotal(so).max(ZERO_DOLLARS);
            report.paymentTotal = report.paymentTotal.add(soPaymentTotal);
        }

        return report;
    }

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

    private SalesReport summarizeSalesOrderRecords(SalesReport report, List<SalesOrderRecord> sos) {

        report.orderCount = sos.size();

        for (SalesOrderRecord so : sos) {

            report.productCount += so.getProductEntryRecords()
                    .stream()
                    .filter(per -> !per.hasStatus(ProductEntryStatus.VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_VOID)
                            && !per.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
                    .count();

            report.chargeCount += so.getChargeEntryRecords()
                    .stream()
                    .filter(cer -> !cer.hasStatus(ChargeEntryStatus.VOID)
                            && !cer.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                    .count();

            report.paymentCount += so.getPaymentEntryRecords()
                    .stream()
                    .filter(per -> !per.hasStatus(PaymentEntryStatus.VOID)
                            && !per.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    .count();

            for (PaymentEntryRecord pe : so.getPaymentEntryRecords()) {
                if (pe.hasStatus(PaymentEntryStatus.VOID) || pe.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    continue;

                switch (pe.getType()) {
                    case CASH:
                        report.cashCount++;
                        report.cashTotal = report.cashTotal.add(pe.getAmount());
                        break;
                    case CREDIT:
                        report.creditCount++;
                        report.creditTotal = report.creditTotal.add(pe.getAmount());
                        break;
                    case CHECK:
                        report.checkCount++;
                        report.checkTotal = report.checkTotal.add(pe.getAmount());
                        break;
                    case GIFTCARD:
                        report.giftcardCount++;
                        report.giftcardTotal = report.giftcardTotal.add(pe.getAmount());
                        break;
                }
            }

            report.gratuityCount += so.hasGratuity() ? 1 : 0;

            BigDecimal soProductTotal = salesOrderRecordProductEntryRecordTotal(so).max(ZERO_DOLLARS);
            report.productTotal = report.productTotal.add(soProductTotal);

            BigDecimal soChargeTotal = salesOrderRecordChargeTotal(so, soProductTotal);
            report.chargeTotal = report.chargeTotal.add(soChargeTotal);

            BigDecimal soSubTotal = soProductTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            report.subTotal = report.subTotal.add(soSubTotal);

            BigDecimal soTaxTotal = salesOrderRecordTaxTotal(so, soSubTotal).max(ZERO_DOLLARS);
            report.taxTotal = report.taxTotal.add(soTaxTotal);

            BigDecimal soGratuityTotal = salesOrderRecordGratuityTotal(so, soSubTotal).max(ZERO_DOLLARS);
            report.gratuityTotal = report.gratuityTotal.add(soGratuityTotal);

            BigDecimal soGrandTotal = soSubTotal.add(soTaxTotal).add(soGratuityTotal).max(ZERO_DOLLARS);
            report.grandTotal = report.grandTotal.add(soGrandTotal);

            BigDecimal soPaymentTotal = salesOrderRecordPaymentTotal(so).max(ZERO_DOLLARS);
            report.paymentTotal = report.paymentTotal.add(soPaymentTotal);
        }

        return report;
    }

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
