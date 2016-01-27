package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;
import ow.micropos.server.model.enums.ChargeType;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.records.ChargeEntryRecord;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Charge {

    @Id
    @GeneratedValue
    @JsonView(View.Charge.class)
    Long id;

    @JsonView(View.ChargeAll.class)
    Date date;

    @JsonView(View.ChargeAll.class)
    boolean archived;

    @JsonView(View.ChargeAll.class)
    Date archiveDate;

    @JsonView(View.Charge.class)
    int weight;

    @JsonView(View.Charge.class)
    String name;

    @JsonView(View.Charge.class)
    String tag;

    @JsonView(View.Charge.class)
    BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.Charge.class)
    ChargeType type;

    @JsonView(View.ChargeWithChargeEntry.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "charge")
    List<ChargeEntry> chargeEntries;

    @JsonView(View.ChargeWithChargeEntryRecord.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "charge")
    List<ChargeEntryRecord> chargeEntryRecords;

    public boolean hasType(ChargeType type) {
        return getType() == type;
    }

}
