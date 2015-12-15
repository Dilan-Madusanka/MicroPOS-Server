package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @JsonView(View.MenuItem.class)
    @ElementCollection(fetch = FetchType.LAZY)
    List<String> printers;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductEntry> getProductEntries() {
        return productEntries;
    }

    public void setProductEntries(List<ProductEntry> productEntries) {
        this.productEntries = productEntries;
    }

    public List<String> getPrinters() {
        return printers;
    }

    public void setPrinters(List<String> printers) {
        this.printers = printers;
    }
}
