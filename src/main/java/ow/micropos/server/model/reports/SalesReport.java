package ow.micropos.server.model.reports;


import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;

import java.math.BigDecimal;
import java.util.Date;

public class SalesReport {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    public SalesReport(Date start, Date end, SalesOrderStatus status, SalesOrderType type) {
        this.start = start;
        this.end = end;
        this.status = status;
        this.type = type;
    }

    public final SalesOrderStatus status;
    public final SalesOrderType type;
    public final Date start;
    public final Date end;

    public int orderCount = 0;
    public int gratuityCount = 0;
    public int productCount = 0;
    public int chargeCount = 0;
    public int paymentCount = 0;
    public int cashCount = 0;
    public int creditCount = 0;
    public int checkCount = 0;
    public int giftcardCount = 0;

    public BigDecimal productTotal = ZERO_DOLLARS;
    public BigDecimal chargeTotal = ZERO_DOLLARS;
    public BigDecimal subTotal = ZERO_DOLLARS;
    public BigDecimal taxTotal = ZERO_DOLLARS;
    public BigDecimal gratuityTotal = ZERO_DOLLARS;
    public BigDecimal grandTotal = ZERO_DOLLARS;
    public BigDecimal paymentTotal = ZERO_DOLLARS;
    public BigDecimal cashTotal = ZERO_DOLLARS;
    public BigDecimal creditTotal = ZERO_DOLLARS;
    public BigDecimal checkTotal = ZERO_DOLLARS;
    public BigDecimal giftcardTotal = ZERO_DOLLARS;

}
