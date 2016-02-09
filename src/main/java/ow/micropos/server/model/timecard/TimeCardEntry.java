package ow.micropos.server.model.timecard;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;
import ow.micropos.server.model.employee.Employee;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class TimeCardEntry {

    public TimeCardEntry() {}

    public static TimeCardEntry employeeEntry(Employee employee, boolean clockin) {
        TimeCardEntry entry = new TimeCardEntry();
        entry.clockin = clockin;
        entry.date = new Date();
        entry.employee = employee;
        entry.verifier = null;
        return entry;
    }

    public static TimeCardEntry verifiedEntry(Employee employee, Employee verifier, Date date, boolean clockin) {
        TimeCardEntry entry = new TimeCardEntry();
        entry.clockin = clockin;
        entry.date = date;
        entry.employee = employee;
        entry.verifier = verifier;
        return entry;
    }

    @Id
    @GeneratedValue
    @JsonView(View.TimeCardEntry.class)
    Long id;

    @JsonView(View.TimeCardEntry.class)
    boolean clockin;

    @JsonView(View.TimeCardEntryAll.class)
    boolean archived;

    @JsonView(View.TimeCardEntryAll.class)
    Date archiveDate;

    @JsonView(View.TimeCardEntry.class)
    Date date;

    @JsonView(View.TimeCardEntryWithEmployee.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Employee employee;

    @JsonView(View.TimeCardEntryWithEmployee.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Employee verifier;

}
