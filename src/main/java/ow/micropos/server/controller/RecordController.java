package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.model.View;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.RecordService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/records")
public class RecordController {

    @Autowired RecordService rService;
    @Autowired AuthService authService;

    @JsonView(value = View.SalesOrderRecordDetails.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<SalesOrderRecord> getSalesOrderRecords(
            HttpServletRequest request,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date
                    start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date end
    ) {

        authService.authorize(request, Permission.GET_RECORDS);

        if (start == null && end == null) {

            return rService.findSalesOrderRecords();

        } else {

            if (start == null)
                start = new Date(Long.MIN_VALUE);

            if (end == null)
                end = new Date(Long.MAX_VALUE);

            return rService.findSalesOrderRecords(start, end);
        }
    }

}
