package ow.micropos.server.model.auth;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.employee.Employee;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Position {

    @Id
    @GeneratedValue
    @JsonView(View.Position.class)
    Long id;

    @JsonView(View.Position.class)
    String name;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.Position.class)
    @ElementCollection(fetch = FetchType.LAZY)
    List<Permission> permissions;

    @JsonView(View.PositionWithEmployee.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "positions")
    List<Employee> employees;

}
