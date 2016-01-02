package ow.micropos.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.exception.MicroPosException;
import ow.micropos.server.model.Permission;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.MigrationService;
import ow.micropos.server.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/migration")
public class MigrationController {

    @Autowired OrderService oService;
    @Autowired MigrationService mService;
    @Autowired AuthService authService;

    /******************************************************************
     *                                                                *
     * Move all sales orders to sales order records. Reset sales      *
     * order tables.                                                  *
     *                                                                *
     * Return : Number of sales orders migrated.                      *
     *                                                                *
     ******************************************************************/

    @RequestMapping(value = "", method = RequestMethod.GET)
    public int migrateSalesOrders(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.MIGRATION);

        return mService.migrateSalesOrders();
    }

}
