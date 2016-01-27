package ow.micropos.server.model.employee;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.records.SalesOrderRecord;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue
    @JsonView(View.Employee.class)
    Long id;

    @JsonView(View.EmployeeAll.class)
    Date date;

    @JsonView(View.EmployeeAll.class)
    boolean archived;

    @JsonView(View.EmployeeAll.class)
    Date archiveDate;

    @JsonView(View.Employee.class)
    String pin;

    @JsonView(View.Employee.class)
    String firstName;

    @JsonView(View.Employee.class)
    String lastName;

    @JsonView(View.EmployeeWithSalesOrder.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    List<SalesOrder> salesOrders;

    @JsonView(View.EmployeeWithSalesOrderRecord.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    List<SalesOrderRecord> salesOrderRecords;

    @JsonView(View.EmployeeWithPosition.class)
    @ManyToMany(fetch = FetchType.LAZY)
    List<Position> positions;

    public boolean hasPermission(Permission permission) {
        for (Position position : positions) {
            if (position.getPermissions().contains(permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPermissions(Permission... permissions) {
        for (Permission reqPermission : permissions) {
            if (!hasPermission(reqPermission)) {
                return false;
            }
        }
        return true;
    }

    public boolean isOwnerOf(SalesOrder salesOrder) {
        return (id != null)
                && (salesOrder.getEmployee() != null)
                && Objects.equals(id, salesOrder.getEmployee().getId());
    }

    public boolean isOwnerOf(List<SalesOrder> salesOrders) {
        for (SalesOrder salesOrder : salesOrders) {
            if (!isOwnerOf(salesOrder)) {
                return false;
            }
        }
        return true;
    }

}
