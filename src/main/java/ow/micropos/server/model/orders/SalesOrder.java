package ow.micropos.server.model.orders;

import com.fasterxml.jackson.annotation.JsonView;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.target.Seat;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class SalesOrder {

    @Id
    @GeneratedValue
    @JsonView(View.SalesOrder.class)
    Long id;

    @JsonView(View.SalesOrderEmployee.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Employee employee;

    @JsonView(View.SalesOrderTarget.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Customer customer;

    @JsonView(View.SalesOrderTarget.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Seat seat;

    @JsonView(View.SalesOrder.class)
    Date date;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.SalesOrder.class)
    SalesOrderType type;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.SalesOrder.class)
    SalesOrderStatus status;

    @JsonView(View.SalesOrderDetails.class)
    BigDecimal taxPercent;

    @JsonView(View.SalesOrderDetails.class)
    BigDecimal gratuityPercent;

    @JsonView(View.SalesOrderDetails.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrder")
    List<ProductEntry> productEntries;

    @JsonView(View.SalesOrderDetails.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrder")
    List<ChargeEntry> chargeEntries;

    @JsonView(View.SalesOrderDetails.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrder")
    List<PaymentEntry> paymentEntries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SalesOrderType getType() {
        return type;
    }

    public void setType(SalesOrderType type) {
        this.type = type;
    }

    public SalesOrderStatus getStatus() {
        return status;
    }

    public void setStatus(SalesOrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimal getGratuityPercent() {
        return gratuityPercent;
    }

    public void setGratuityPercent(BigDecimal gratuityPercent) {
        this.gratuityPercent = gratuityPercent;
    }

    public List<ProductEntry> getProductEntries() {
        return productEntries;
    }

    public void setProductEntries(List<ProductEntry> productEntries) {
        this.productEntries = productEntries;
    }

    public List<ChargeEntry> getChargeEntries() {
        return chargeEntries;
    }

    public void setChargeEntries(List<ChargeEntry> chargeEntries) {
        this.chargeEntries = chargeEntries;
    }

    public List<PaymentEntry> getPaymentEntries() {
        return paymentEntries;
    }

    public void setPaymentEntries(List<PaymentEntry> paymentEntries) {
        this.paymentEntries = paymentEntries;
    }

    public boolean hasStatus(SalesOrderStatus status) {
        return getStatus() == status;
    }

    public boolean hasType(SalesOrderType type) {
        return getType() == type;
    }

    public boolean hasGratuity() {
        return getGratuityPercent() != null && getGratuityPercent().compareTo(BigDecimal.ZERO) != 0;
    }

    public String getSummary() {
        String summary = "";
        for (ProductEntry pe : getProductEntries()) {
            summary += pe.getQuantity() + " " + pe.getMenuItem().getName() + "\n";
            for (Modifier m : pe.getModifiers())
                summary += "\t" + m.getName() + "\n";
        }
        return summary;
    }

}
