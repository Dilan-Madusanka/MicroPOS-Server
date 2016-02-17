package ow.micropos.server.model.timecard;

import lombok.Data;
import ow.micropos.server.model.employee.Employee;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TimeCard {

    private static final SimpleDateFormat keyFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static class WorkSession {
        public TimeCardEntry startEntry, endEntry;
    }

    public static class WorkDay {
        public Date date;
        public List<WorkSession> sessions;
        public int minutes;


    }

    public Employee employee;
    public Map<String, WorkDay> workDays;

    public static TimeCard fromEntries(Employee employee, List<TimeCardEntry> entries) {

        TimeCard timeCard = new TimeCard();
        timeCard.employee = employee;
        timeCard.workDays = new HashMap<>();

        entries.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
        for (TimeCardEntry e : entries) {

        }

        return timeCard;

    }

}
