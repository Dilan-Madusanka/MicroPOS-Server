package ow.micropos.server.model.reports;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActiveSalesReport {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    public ActiveSalesReport(Date date) {
        this.date = date;
        closedSummary = new Summary();
        openSummary = new Summary();
        voidSummary = new Summary();
    }

    public final Date date;

    public Summary closedSummary;
    public Summary openSummary;
    public Summary voidSummary;

}
