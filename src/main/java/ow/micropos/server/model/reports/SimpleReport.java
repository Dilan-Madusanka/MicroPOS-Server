package ow.micropos.server.model.reports;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;

import java.math.BigDecimal;

@Data
public class SimpleReport {

    public SimpleReport() {}

    public SimpleReport(Integer orderCount, BigDecimal payTotal, BigDecimal prodTotal) {
        this.orderCount = orderCount;
        this.payTotal = payTotal;
        this.prodTotal = prodTotal;
    }

    @JsonView(View.SimpleReport.class)
    Integer orderCount;

    @JsonView(View.SimpleReport.class)
    BigDecimal payTotal;

    @JsonView(View.SimpleReport.class)
    BigDecimal prodTotal;

}
