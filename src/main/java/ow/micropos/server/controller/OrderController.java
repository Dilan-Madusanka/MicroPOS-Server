package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.OrderService;
import ow.micropos.server.service.SalesOrderService;
import ow.micropos.server.service.SplitService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired AuthService authService;
    @Autowired SalesOrderService soService;
    @Autowired OrderService orderService;
    @Autowired SplitService splitService;

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<SalesOrder> getSalesOrders(
            HttpServletRequest request,
            @RequestParam(value = "status", required = false) SalesOrderStatus status,
            @RequestParam(value = "type", required = false) SalesOrderType type
    ) {

        Employee employee = authService.authorize(request, Permission.GET_SALES_ORDERS);

        /*
        TODO : Customize results based on permissions.

        boolean canTakeOut = employee.hasPermission(Permission.GET_TAKE_OUT_SALES_ORDERS);
        boolean canDineIn = employee.hasPermission(Permission.GET_DINE_IN_SALES_ORDERS);

        if (type == SalesOrderType.DINEIN)
        */

        List<SalesOrder> result = soService.findSalesOrders(status, type);
        result.forEach(soService::filterVoidProductEntries);
        return result;
    }

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "/seat", method = RequestMethod.GET)
    public List<SalesOrder> getSalesOrdersBySeat(
            HttpServletRequest request,
            @RequestParam(value = "id", required = true) long id,
            @RequestParam(value = "status", required = false) SalesOrderStatus status
    ) {

        authService.authorize(request, Permission.GET_DINE_IN_SALES_ORDERS);

        List<SalesOrder> result = soService.findSalesOrdersBySeat(id, status);
        result.forEach(soService::filterVoidProductEntries);
        return result;

    }

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public List<SalesOrder> getSalesOrderByCustomer(
            HttpServletRequest request,
            @RequestParam(value = "id", required = true) long id,
            @RequestParam(value = "status", required = false) SalesOrderStatus status
    ) {

        authService.authorize(request, Permission.GET_TAKE_OUT_SALES_ORDERS);

        List<SalesOrder> result = soService.findSalesOrdersByCustomer(id, status);
        result.forEach(soService::filterVoidProductEntries);
        return result;

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public long postSalesOrder(
            HttpServletRequest request,
            @RequestBody(required = true) SalesOrder salesOrder
    ) {

        Employee employee = authService.authorize(request);

        if (!employee.isOwnerOf(salesOrder))
            authService.authorize(employee, Permission.ACCESS_ALL_EMPLOYEE_ORDER);

        return orderService.order(employee, salesOrder);

    }

    @RequestMapping(value = "/split", method = RequestMethod.POST)
    public List<Long> postSalesOrders(
            HttpServletRequest request,
            @RequestBody(required = true) List<SalesOrder> salesOrders
    ) {

        Employee employee = authService.authorize(request);

        if (!employee.isOwnerOf(salesOrders))
            authService.authorize(employee, Permission.ACCESS_ALL_EMPLOYEE_ORDER);

        return splitService.splitOrder(employee, salesOrders);

    }


}
