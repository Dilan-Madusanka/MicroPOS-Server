package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.timecard.TimeCardEntry;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.GeneralService;
import ow.micropos.server.service.TimeCardService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/timecard")
public class TimeCardController {

    @Autowired GeneralService genService;
    @Autowired AuthService authService;
    @Autowired TimeCardService tcService;

    @JsonView(value = View.TimeCardEntry.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TimeCardEntry> getTimeCard(@RequestParam(value = "pin", required = true) String pin) {

        Employee employee = genService.getEmployee(pin);

        authService.authorize(employee, Permission.VIEW_TIME_CARD);

        return tcService.getTimeCardEntries(employee);

    }

    @JsonView(value = View.TimeCardEntry.class)
    @RequestMapping(value = "/clockin", method = RequestMethod.POST)
    public TimeCardEntry clockIn(
            @RequestParam(value = "pin", required = true) String pin,
            @RequestBody(required = false) String img
    ) {

        Employee employee = genService.getEmployee(pin);

        authService.authorize(employee, Permission.CLOCK_IN);

        return tcService.standardEntry(employee, img, true);

    }

    @JsonView(value = View.TimeCardEntry.class)
    @RequestMapping(value = "/clockout", method = RequestMethod.POST)
    public TimeCardEntry clockOut(
            @RequestParam(value = "pin", required = true) String pin,
            @RequestBody(required = false) String img
    ) {

        Employee employee = genService.getEmployee(pin);

        authService.authorize(employee, Permission.CLOCK_OUT);

        return tcService.standardEntry(employee, img, false);

    }

    @JsonView(value = View.TimeCardEntry.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public TimeCardEntry createEntry(HttpServletRequest request, @RequestBody(required = true) TimeCardEntry entry) {

        Employee verifier = authService.authorize(request, Permission.TIME_CARD_CREATE);

        return tcService.verifiedEntry(entry.getEmployee(), entry.isClockin(), entry.getDate(), verifier);

    }

    @JsonView(value = View.TimeCardEntry.class)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public TimeCardEntry updateEntry(HttpServletRequest request, @RequestBody(required = true) TimeCardEntry entry) {

        Employee verifier = authService.authorize(request, Permission.TIME_CARD_UPDATE);

        return tcService.updateEntry(entry.getId(), entry.getDate(), verifier);

    }

    @JsonView(value = View.TimeCardEntry.class)
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public boolean removeEntry(HttpServletRequest request, @RequestParam(value = "id") long id) {

        Employee verifier = authService.authorize(request, Permission.TIME_CARD_DELETE);

        return tcService.archiveEntry(id, verifier);

    }

}
