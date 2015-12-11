package ow.micropos.server.model.records;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;
import ow.micropos.server.model.menu.Charge;
import ow.micropos.server.model.enums.ChargeEntryStatus;
import ow.micropos.server.model.orders.ChargeEntry;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ChargeEntryRecord {

    public ChargeEntryRecord() {}

    public ChargeEntryRecord(ChargeEntry chargeEntry) {
        this.date = chargeEntry.getDate();
        this.charge = chargeEntry.getCharge();
        this.status = chargeEntry.getStatus();
    }

    @Id
    @GeneratedValue
    @JsonView(View.ChargeEntryRecord.class)
    Long id;

    @JsonView(View.ChargeEntryRecordAll.class)
    Date date;

    @JsonView(View.ChargeEntryRecord.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Charge charge;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.ChargeEntryRecord.class)
    ChargeEntryStatus status;

    @JsonView(View.ChargeEntryRecordWithSalesOrderRecord.class)
    @ManyToOne(fetch = FetchType.LAZY)
    SalesOrderRecord salesOrderRecord;

}
