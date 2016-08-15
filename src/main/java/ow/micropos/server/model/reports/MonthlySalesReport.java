package ow.micropos.server.model.reports;

import ow.micropos.server.common.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthlySalesReport {

    public MonthlySalesReport(Date monthOf) {
        this.monthOf = DateUtils.startOfMonth(monthOf);
        this.dailySales = new ArrayList<>();
        this.dailyTax = new ArrayList<>();
        this.netSales = BigDecimal.ZERO;
        this.netTax = BigDecimal.ZERO;
    }

    public Date monthOf;

    public List<BigDecimal> dailySales;

    public List<BigDecimal> dailyTax;

    public BigDecimal netSales;

    public BigDecimal netTax;

}