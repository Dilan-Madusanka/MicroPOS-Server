package ow.micropos.server.model.target;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.View;
import ow.micropos.server.model.records.SalesOrderRecord;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Seat {

    @Id
    @GeneratedValue
    @JsonView(View.Seat.class)
    Long id;

    @JsonView(View.SeatAll.class)
    Date date;

    @JsonView(View.SeatAll.class)
    boolean archived;

    @JsonView(View.SeatAll.class)
    Date archiveDate;

    @JsonView(View.Seat.class)
    String tag;

    @JsonView(View.Seat.class)
    byte row;

    @JsonView(View.Seat.class)
    byte col;

    @JsonView(View.SeatWithSection.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Section section;

    @JsonView(View.SeatWithSalesOrder.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seat")
    List<SalesOrder> salesOrders;

    @JsonView(View.SeatWithSalesOrderRecord.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seat")
    List<SalesOrderRecord> salesOrderRecords;

}
