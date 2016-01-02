package ow.micropos.server.model.records;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.menu.MenuItem;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class ProductEntryRecord {

    public ProductEntryRecord() {}

    public ProductEntryRecord(ProductEntry productEntry) {
        this.date = productEntry.getDate();
        this.status = productEntry.getStatus();
        this.quantity = productEntry.getQuantity();
        this.menuItem = productEntry.getMenuItem();
        this.modifiers = productEntry.getModifiers();
    }

    @Id
    @GeneratedValue
    @JsonView(View.ProductEntryRecord.class)
    Long id;

    @JsonView(View.ProductEntryRecordAll.class)
    Date date;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.ProductEntryRecord.class)
    ProductEntryStatus status;

    @JsonView(View.ProductEntryRecord.class)
    BigDecimal quantity;

    @JsonView(View.ProductEntryRecord.class)
    @ManyToOne(fetch = FetchType.LAZY)
    MenuItem menuItem;

    @JsonView(View.ProductEntryRecord.class)
    @ManyToMany(fetch = FetchType.LAZY)
    List<Modifier> modifiers;

    @JsonView(View.ProductEntryRecordWithSalesOrderRecord.class)
    @ManyToOne(fetch = FetchType.LAZY)
    SalesOrderRecord salesOrderRecord;

    public boolean hasStatus(ProductEntryStatus status) {
        return getStatus() == status;
    }

}
