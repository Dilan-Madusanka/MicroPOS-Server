package ow.micropos.server.model.orders;

import com.fasterxml.jackson.annotation.JsonView;
import ow.micropos.server.model.View;
import ow.micropos.server.model.menu.Charge;
import ow.micropos.server.model.enums.ChargeEntryStatus;
import ow.micropos.server.model.enums.ChargeType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ChargeEntry {

    @Id
    @GeneratedValue
    @JsonView(View.ChargeEntry.class)
    Long id;

    @JsonView(View.ChargeEntryAll.class)
    Date date;

    @JsonView(View.ChargeEntry.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Charge charge;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.ChargeEntry.class)
    ChargeEntryStatus status;

    @JsonView(View.ChargeEntryWithSalesOrder.class)
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

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public ChargeEntryStatus getStatus() {
        return status;
    }

    public void setStatus(ChargeEntryStatus status) {
        this.status = status;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public boolean hasStatus(ChargeEntryStatus status) {
        return getStatus() == status;
    }

    public boolean hasType(ChargeType type) {
        return getCharge().hasType(type);
    }

}
