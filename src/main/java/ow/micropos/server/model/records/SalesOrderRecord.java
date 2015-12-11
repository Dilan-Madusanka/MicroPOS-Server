package ow.micropos.server.model.records;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.target.Seat;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class SalesOrderRecord {

    public SalesOrderRecord() {}

    public SalesOrderRecord(SalesOrder salesOrder) {
        this.employee = salesOrder.getEmployee();
        this.customer = salesOrder.getCustomer();
        this.seat = salesOrder.getSeat();
        this.date = salesOrder.getDate();
        this.type = salesOrder.getType();
        this.status = salesOrder.getStatus();
        this.taxPercent = salesOrder.getTaxPercent();
        this.gratuityPercent = salesOrder.getGratuityPercent();

        this.chargeEntryRecords = salesOrder.getChargeEntries()
                .stream()
                .map(ce -> {
                    ChargeEntryRecord cer = new ChargeEntryRecord(ce);
                    cer.setSalesOrderRecord(this);
                    return cer;
                })
                .collect(Collectors.toList());

        this.productEntryRecords = salesOrder.getProductEntries()
                .stream()
                .map(pe -> {
                    ProductEntryRecord per = new ProductEntryRecord(pe);
                    per.setSalesOrderRecord(this);
                    return per;
                })
                .collect(Collectors.toList());

        this.paymentEntryRecords = salesOrder.getPaymentEntries()
                .stream()
                .map(pe -> {
                    PaymentEntryRecord per = new PaymentEntryRecord(pe);
                    per.setSalesOrderRecord(this);
                    return per;
                })
                .collect(Collectors.toList());

    }

    @Id
    @GeneratedValue
    @JsonView(View.SalesOrderRecord.class)
    Long id;

    @JsonView(View.SalesOrderRecordEmployee.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Employee employee;

    @JsonView(View.SalesOrderRecordTarget.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Customer customer;

    @JsonView(View.SalesOrderRecordTarget.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Seat seat;

    @JsonView(View.SalesOrderRecord.class)
    Date date;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.SalesOrderRecord.class)
    SalesOrderType type;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.SalesOrderRecord.class)
    SalesOrderStatus status;

    @JsonView(View.SalesOrderRecordDetails.class)
    BigDecimal taxPercent;

    @JsonView(View.SalesOrderRecordDetails.class)
    BigDecimal gratuityPercent;

    @JsonView(View.SalesOrderRecordDetails.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrderRecord")
    List<ProductEntryRecord> productEntryRecords;

    @JsonView(View.SalesOrderRecordDetails.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrderRecord")
    List<PaymentEntryRecord> paymentEntryRecords;

    @JsonView(View.SalesOrderRecordDetails.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrderRecord")
    List<ChargeEntryRecord> chargeEntryRecords;

}
