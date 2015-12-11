package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;
import ow.micropos.server.model.enums.ChargeType;
import ow.micropos.server.model.orders.ChargeEntry;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ChargeType getType() {
        return type;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }

    public List<ChargeEntry> getChargeEntries() {
        return chargeEntries;
    }

    public void setChargeEntries(List<ChargeEntry> chargeEntries) {
        this.chargeEntries = chargeEntries;
    }

    public boolean hasType(ChargeType type) {
        return getType() == type;
    }

}
