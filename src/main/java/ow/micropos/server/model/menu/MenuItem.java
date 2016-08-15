package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.View;
import ow.micropos.server.model.records.ProductEntryRecord;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class MenuItem {

    @Id
    @GeneratedValue
    @JsonView(View.MenuItem.class)
    Long id;

    @JsonView(View.MenuItemAll.class)
    Date date;

    @JsonView(View.MenuItemAll.class)
    boolean archived;

    @JsonView(View.MenuItemAll.class)
    Date archiveDate;

    @JsonView(View.MenuItem.class)
    int weight;

    @JsonView(View.MenuItem.class)
    String name;

    @JsonView(View.MenuItem.class)
    String tag;

    @JsonView(View.MenuItem.class)
    BigDecimal price;

    @JsonView(View.MenuItem.class)
    boolean taxed;

    @JsonView(View.MenuItemWithCategory.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    @JsonView(View.MenuItemWithSalesOrder.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuItem")
    List<ProductEntry> productEntries;

    @JsonView(View.MenuItemWithSalesOrderRecord.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuItem")
    List<ProductEntryRecord> productEntryRecords;

    @JsonView(View.MenuItem.class)
    @ElementCollection(fetch = FetchType.LAZY)
    List<String> printers;

}
