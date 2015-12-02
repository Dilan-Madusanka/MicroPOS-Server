package ow.micropos.server.model.orders;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.menu.MenuItem;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class ProductEntry {

    @Id
    @GeneratedValue
    @JsonView(View.ProductEntry.class)
    Long id;

    @JsonView(View.ProductEntryAll.class)
    Date date;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.ProductEntry.class)
    ProductEntryStatus status;

    @JsonView(View.ProductEntry.class)
    BigDecimal quantity;

    @JsonView(View.ProductEntry.class)
    @ManyToOne(fetch = FetchType.LAZY)
    MenuItem menuItem;

    @JsonView(View.ProductEntry.class)
    @ManyToMany(fetch = FetchType.LAZY)
    List<Modifier> modifiers;

    @JsonView(View.ProductEntryWithSalesOrder.class)
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

    public ProductEntryStatus getStatus() {
        return status;
    }

    public void setStatus(ProductEntryStatus status) {
        this.status = status;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public boolean hasStatus(ProductEntryStatus status) {
        return getStatus() == status;
    }

}
