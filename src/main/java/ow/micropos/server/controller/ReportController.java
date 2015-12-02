package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.reports.CurrentSalesReport;
import ow.micropos.server.model.reports.SimpleReport;
import ow.micropos.server.model.View;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired ReportService rService;
    @Autowired AuthService authService;

    @JsonView(View.SimpleReport.class)
    @RequestMapping(value = "/simple", method = RequestMethod.GET)
    public SimpleReport generateSimpleReport(
            HttpServletRequest request,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date
                    start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date end
    ) {

        authService.authorize(request, Permission.SIMPLE_REPORT);

        if (start == null)
            start = new Date(Long.MIN_VALUE);

        if (end == null)
            end = new Date(Long.MAX_VALUE);

        return rService.generateSimpleReport(start, end);

    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public CurrentSalesReport generateCurrentSalesReport(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.CURRENT_REPORT);

        return rService.generateCurrentSalesReport();

    }

}
