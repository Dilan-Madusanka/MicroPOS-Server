package ow.micropos.server.model.orders;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;
import ow.micropos.server.model.enums.PaymentEntryStatus;
import ow.micropos.server.model.enums.PaymentEntryType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class PaymentEntry {

    @Id
    @GeneratedValue
    @JsonView(View.PaymentEntry.class)
    Long id;

    @JsonView(View.PaymentEntryAll.class)
    Date date;

    @JsonView(View.PaymentEntry.class)
    BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.PaymentEntry.class)
    PaymentEntryStatus status;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.PaymentEntry.class)
    PaymentEntryType type;

    @JsonView(View.PaymentEntryWithSalesOrder.class)
    @ManyToOne(fetch = FetchType.LAZY)
    SalesOrder salesOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentEntryStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentEntryStatus status) {
        this.status = status;
    }

    public PaymentEntryType getType() {
        return type;
    }

    public void setType(PaymentEntryType type) {
        this.type = type;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public boolean hasStatus(PaymentEntryStatus status) {
        return getStatus() == status;
    }
}
