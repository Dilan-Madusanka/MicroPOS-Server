package ow.micropos.server.model.reports;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DaySalesReport {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    public DaySalesReport(Date start, Date end) {
        this.start = start;
        this.end = end;
        closedSummary = new Summary();
        openSummary = new Summary();
        voidSummary = new Summary();
    }

    public final Date start, end;

    public Summary closedSummary;
    public Summary openSummary;
    public Summary voidSummary;

}
