package ow.micropos.server.model.reports;


import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public int dineInCount = 0;
    public int takeOutCount = 0;

    public BigDecimal total = ZERO_DOLLARS;

    public BigDecimal paymentTotal = ZERO_DOLLARS;
    public BigDecimal cashTotal = ZERO_DOLLARS;
    public BigDecimal creditTotal = ZERO_DOLLARS;
    public BigDecimal checkTotal = ZERO_DOLLARS;
    public BigDecimal giftcardTotal = ZERO_DOLLARS;

    public BigDecimal taxTotal = ZERO_DOLLARS;
    public BigDecimal chargeTotal = ZERO_DOLLARS;

    public BigDecimal taxedSalesTotal = ZERO_DOLLARS;
    public BigDecimal untaxedSalesTotal = ZERO_DOLLARS;
    public BigDecimal salesTotal = ZERO_DOLLARS;

    public HashMap<String, BigDecimal> categorySalesTotals = new HashMap<>();

}