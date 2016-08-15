package ow.micropos.server.service;

import ow.micropos.server.common.DateUtils;
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

@SuppressWarnings("Duplicates")
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

            /*
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
            */

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

            if (so.hasType(SalesOrderType.DINEIN))
                report.dineInCount++;
            else
                report.takeOutCount++;

            // Main Stuff

            BigDecimal soTaxedSalesTotal = ZERO_DOLLARS;
            BigDecimal soUntaxedSalesTotal = ZERO_DOLLARS;
            for (ProductEntry pe : so.getProductEntries()) {
                if (pe.hasStatus(ProductEntryStatus.VOID)
                        || pe.hasStatus(ProductEntryStatus.REQUEST_VOID)
                        || pe.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
                    continue;

                BigDecimal peTotal = productEntryTotal(pe);

                if (pe.getMenuItem().isTaxed()) {
                    soTaxedSalesTotal = soTaxedSalesTotal
                            .add(peTotal)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                } else {
                    soUntaxedSalesTotal = soUntaxedSalesTotal
                            .add(peTotal)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                // Feels Dirty >>> Calculate Category Totals
                String peCategory = pe.getMenuItem().getCategory().getName();
                BigDecimal categoryTotal = report.categorySalesTotals.get(peCategory);
                if (categoryTotal == null)
                    report.categorySalesTotals.put(peCategory, peTotal);
                else
                    report.categorySalesTotals.put(peCategory, peTotal.add(categoryTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
                // Feels Dirty <<< Calculate Category Totals

            }

            BigDecimal soChargeTotal = ZERO_DOLLARS;
            for (ChargeEntry ce : so.getChargeEntries()) {
                if (ce.hasStatus(ChargeEntryStatus.VOID) || ce.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                    continue;

                if (ce.hasType(ChargeType.FIXED_AMOUNT)) {
                    soChargeTotal = soChargeTotal
                            .add(ce.getCharge().getAmount())
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                } else if (ce.hasType(ChargeType.PERCENTAGE)) {
                    soChargeTotal = soChargeTotal
                            .add(ce.getCharge().getAmount().multiply(soTaxedSalesTotal))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }
            }

            BigDecimal soSubtotal = soTaxedSalesTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            BigDecimal soTaxTotal = soSubtotal.multiply(so.getTaxPercent()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal soTotal = soSubtotal.add(soTaxTotal).add(soUntaxedSalesTotal);

            BigDecimal effectiveChargeTotal;
            if (soTaxedSalesTotal.add(soChargeTotal).compareTo(BigDecimal.ZERO) < 0) {
                effectiveChargeTotal = soTaxedSalesTotal;
            } else {
                effectiveChargeTotal = soChargeTotal;
            }

            report.salesTotal = report.salesTotal.add(soTaxedSalesTotal).add(soUntaxedSalesTotal);
            report.taxedSalesTotal = report.taxedSalesTotal.add(soTaxedSalesTotal);
            report.untaxedSalesTotal = report.untaxedSalesTotal.add(soUntaxedSalesTotal);
            report.taxTotal = report.taxTotal.add(soTaxTotal);
            report.chargeTotal = report.chargeTotal.add(effectiveChargeTotal);
            report.total = report.total.add(soTotal);


            BigDecimal paymentLeft = soTotal;
            for (PaymentEntry pe : so.getPaymentEntries()) {
                if (pe.hasStatus(PaymentEntryStatus.VOID) || pe.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    continue;

                if (paymentLeft.compareTo(ZERO_DOLLARS) <= 0)
                    break;

                BigDecimal appliedPayment = paymentLeft.min(pe.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                paymentLeft = paymentLeft.subtract(appliedPayment);

                report.paymentTotal = report.paymentTotal.add(appliedPayment);

                switch (pe.getType()) {
                    case CASH:
                        report.cashTotal = report.cashTotal.add(appliedPayment);
                        break;
                    case CREDIT:
                        report.creditTotal = report.creditTotal.add(appliedPayment);
                        break;
                    case CHECK:
                        report.checkTotal = report.checkTotal.add(appliedPayment);
                        break;
                    case GIFTCARD:
                        report.giftcardTotal = report.giftcardTotal.add(appliedPayment);
                        break;
                }
            }

        }

        return report;
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

            if (so.hasType(SalesOrderType.DINEIN))
                report.dineInCount++;
            else
                report.takeOutCount++;

            // Main Stuff

            BigDecimal soTaxedSalesTotal = ZERO_DOLLARS;
            BigDecimal soUntaxedSalesTotal = ZERO_DOLLARS;
            for (ProductEntryRecord pe : so.getProductEntryRecords()) {
                if (pe.hasStatus(ProductEntryStatus.VOID)
                        || pe.hasStatus(ProductEntryStatus.REQUEST_VOID)
                        || pe.hasStatus(ProductEntryStatus.REQUEST_HOLD_VOID))
                    continue;

                BigDecimal peTotal = productEntryRecordTotal(pe);

                if (pe.getMenuItem().isTaxed()) {
                    soTaxedSalesTotal = soTaxedSalesTotal
                            .add(peTotal)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                } else {
                    soUntaxedSalesTotal = soUntaxedSalesTotal
                            .add(peTotal)
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                // Feels Dirty >>> Calculate Category Totals
                String peCategory = pe.getMenuItem().getCategory().getName();
                BigDecimal categoryTotal = report.categorySalesTotals.get(peCategory);
                if (categoryTotal == null)
                    report.categorySalesTotals.put(peCategory, peTotal);
                else
                    report.categorySalesTotals.put(peCategory, peTotal.add(categoryTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
                // Feels Dirty <<< Calculate Category Totals

            }

            BigDecimal soChargeTotal = ZERO_DOLLARS;
            for (ChargeEntryRecord ce : so.getChargeEntryRecords()) {
                if (ce.hasStatus(ChargeEntryStatus.VOID) || ce.hasStatus(ChargeEntryStatus.REQUEST_VOID))
                    continue;

                if (ce.hasType(ChargeType.FIXED_AMOUNT)) {
                    soChargeTotal = soChargeTotal
                            .add(ce.getCharge().getAmount())
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                } else if (ce.hasType(ChargeType.PERCENTAGE)) {
                    soChargeTotal = soChargeTotal
                            .add(ce.getCharge().getAmount().multiply(soTaxedSalesTotal))
                            .setScale(2, BigDecimal.ROUND_HALF_UP);
                }
            }

            BigDecimal soSubtotal = soTaxedSalesTotal.add(soChargeTotal).max(ZERO_DOLLARS);
            BigDecimal soTaxTotal = soSubtotal.multiply(so.getTaxPercent()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal soTotal = soSubtotal.add(soTaxTotal).add(soUntaxedSalesTotal);

            BigDecimal effectiveChargeTotal;
            if (soTaxedSalesTotal.add(soChargeTotal).compareTo(BigDecimal.ZERO) < 0) {
                effectiveChargeTotal = soTaxedSalesTotal;
            } else {
                effectiveChargeTotal = soChargeTotal;
            }

            report.salesTotal = report.salesTotal.add(soTaxedSalesTotal).add(soUntaxedSalesTotal);
            report.taxedSalesTotal = report.taxedSalesTotal.add(soTaxedSalesTotal);
            report.untaxedSalesTotal = report.untaxedSalesTotal.add(soUntaxedSalesTotal);
            report.taxTotal = report.taxTotal.add(soTaxTotal);
            report.chargeTotal = report.chargeTotal.add(effectiveChargeTotal);
            report.total = report.total.add(soTotal);


            BigDecimal paymentLeft = soTotal;
            for (PaymentEntryRecord pe : so.getPaymentEntryRecords()) {
                if (pe.hasStatus(PaymentEntryStatus.VOID) || pe.hasStatus(PaymentEntryStatus.REQUEST_VOID))
                    continue;

                if (paymentLeft.compareTo(ZERO_DOLLARS) <= 0)
                    break;

                BigDecimal appliedPayment = paymentLeft.min(pe.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                paymentLeft = paymentLeft.subtract(appliedPayment);

                report.paymentTotal = report.paymentTotal.add(appliedPayment);

                switch (pe.getType()) {
                    case CASH:
                        report.cashTotal = report.cashTotal.add(appliedPayment);
                        break;
                    case CREDIT:
                        report.creditTotal = report.creditTotal.add(appliedPayment);
                        break;
                    case CHECK:
                        report.checkTotal = report.checkTotal.add(appliedPayment);
                        break;
                    case GIFTCARD:
                        report.giftcardTotal = report.giftcardTotal.add(appliedPayment);
                        break;
                }
            }

        }

        return report;
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
