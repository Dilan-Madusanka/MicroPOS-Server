package ow.micropos.server.model.reports;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CurrentSalesReport {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    public int orderCount = 0;
    public int openCount = 0;
    public int closedCount = 0;
    public int voidCount = 0;

    public int dineInCount = 0;
    public int takeOutCount = 0;

    public int productCount = 0;
    public int productVoidCount = 0;

    public BigDecimal productTotal = ZERO_DOLLARS;
    public List<CategoryReport> categoryReports = new ArrayList<>();

    public BigDecimal chargeTotal = ZERO_DOLLARS;
    public int chargeCount = 0;
    public List<ChargeReport> chargeReports = new ArrayList<>();

    public BigDecimal subTotal = ZERO_DOLLARS;
    public BigDecimal taxTotal = ZERO_DOLLARS;
    public BigDecimal gratuityTotal = ZERO_DOLLARS;
    public BigDecimal grandTotal = ZERO_DOLLARS;

    public BigDecimal paymentTotal = ZERO_DOLLARS;
    public int paymentCount = 0;
    public List<PaymentReport> paymentReports = new ArrayList<>();

    public BigDecimal changeTotal = ZERO_DOLLARS;

    public BigDecimal netSales = ZERO_DOLLARS;

    @Builder
    public static class CategoryReport {
        public final String categoryName;
        public final BigDecimal categoryTotal;
        public final Integer categoryCount;
    }

    @Builder
    public static class ChargeReport {
        public final String chargeName;
        public final BigDecimal chargeTotal;
        public final Integer chargeCount;
    }

    @Builder
    public static class PaymentReport {
        public final String paymentName;
        public final BigDecimal paymentTotal;
        public final Integer paymentCount;
    }

}
