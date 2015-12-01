package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.View;

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
    String name;

    @JsonView(View.MenuItem.class)
    String tag;

    @JsonView(View.MenuItem.class)
    BigDecimal price;

    @JsonView(View.MenuItemWithCategory.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    @JsonView(View.MenuItemWithSalesOrder.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuItem")
    List<ProductEntry> productEntries;

}
