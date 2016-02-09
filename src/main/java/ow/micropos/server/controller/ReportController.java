package ow.micropos.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.reports.MonthlySalesReport;
import ow.micropos.server.model.reports.SalesReport;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired ReportService rService;
    @Autowired AuthService authService;

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    public SalesReport generateSalesReport(
            HttpServletRequest request,
            @RequestParam(value = "start", required = false) Date start,
            @RequestParam(value = "end", required = false) Date end,
            @RequestParam(value = "status", required = false) SalesOrderStatus status,
            @RequestParam(value = "type", required = false) SalesOrderType type
    ) {

        authService.authorize(request, Permission.SALES_REPORT);

        return rService.generateSalesReport(start, end, status, type);

    }

    @RequestMapping(value = "/monthly", method = RequestMethod.GET)
    public MonthlySalesReport generateMonthlySalesReport(
            HttpServletRequest request,
            @RequestParam(value = "monthOf", required = true) Date monthOf
    ) {

        authService.authorize(request, Permission.SALES_REPORT);

        return rService.generateMonthlySalesReport(monthOf);

    }

}
