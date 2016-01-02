package ow.micropos.server.model.records;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.enums.PaymentEntryStatus;
import ow.micropos.server.model.enums.PaymentEntryType;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class PaymentEntryRecord {

    public PaymentEntryRecord() {}

    public PaymentEntryRecord(PaymentEntry paymentEntry) {
        this.date = paymentEntry.getDate();
        this.amount = paymentEntry.getAmount();
        this.type = paymentEntry.getType();
        this.status = paymentEntry.getStatus();
    }

    @Id
    @GeneratedValue
    @JsonView(View.PaymentEntryRecord.class)
    Long id;

    @JsonView(View.PaymentEntryRecordAll.class)
    Date date;

    @JsonView(View.PaymentEntryRecord.class)
    BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.PaymentEntryRecord.class)
    PaymentEntryStatus status;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.PaymentEntryRecord.class)
    PaymentEntryType type;

    @JsonView(View.PaymentEntryRecordWithSalesOrderRecord.class)
    @ManyToOne(fetch = FetchType.LAZY)
    SalesOrderRecord salesOrderRecord;

    public boolean hasStatus(PaymentEntryStatus status) {
        return getStatus() == status;
    }

    public boolean hasType(PaymentEntryType type) {
        return getType() == type;
    }

}
