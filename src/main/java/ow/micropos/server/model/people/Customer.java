package ow.micropos.server.model.people;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue
    @JsonView(View.Customer.class)
    Long id;

    @JsonView(View.CategoryAll.class)
    Date date;

    @JsonView(View.CategoryAll.class)
    boolean archived;

    @JsonView(View.CategoryAll.class)
    Date archiveDate;

    @JsonView(View.Customer.class)
    String firstName;

    @JsonView(View.Customer.class)
    String lastName;

    @JsonView(View.Customer.class)
    String phoneNumber;

    @JsonView(View.CustomerWithSalesOrder.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    List<SalesOrder> salesOrders;

}
