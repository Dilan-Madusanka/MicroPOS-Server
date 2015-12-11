package ow.micropos.server.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.menu.Charge;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.ModifierGroup;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.target.Section;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.GeneralService;
import ow.micropos.server.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GeneralController {

    @Autowired AuthService authService;
    @Autowired GeneralService genService;
    @Autowired OrderService oService;

    /******************************************************************
     *                                                                *
     * Employee Login
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.EmployeeWithPosition.class)
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public Employee getEmployee(
            HttpServletRequest request,
            @RequestParam(value = "pin", required = true) short pin
    ) {

        //authService.authorize(request);

        return genService.getEmployee(pin);

    }

    /******************************************************************
     *                                                                *
     * Section Requests
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.SectionWithSalesOrder.class)
    @RequestMapping(value = "/sections", method = RequestMethod.GET)
    public List<Section> getSections(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_SECTIONS);

        return genService.getSections(true, true);

    }

    @JsonView(value = View.SectionWithSalesOrder.class)
    @RequestMapping(value = "/sections/{id}", method = RequestMethod.GET)
    public Section getSection(
            HttpServletRequest request,
            @PathVariable(value = "id") long id
    ) {

        authService.authorize(request, Permission.GET_SECTIONS);

        return genService.getSection(id, true, true);

    }

    /******************************************************************
     *                                                                *
     * Customer Requests
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.CustomerWithSalesOrder.class)
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<Customer> getCustomers(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_CUSTOMERS);

        return genService.getCustomers(true, true);

    }

    @JsonView(value = View.CustomerWithSalesOrder.class)
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    public Customer getCustomer(
            HttpServletRequest request,
            @PathVariable(value = "id") long id
    ) {

        authService.authorize(request, Permission.GET_CUSTOMERS);

        return genService.getCustomer(id, true, true);

    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public long postCustomer(
            HttpServletRequest request,
            @RequestBody(required = true) Customer customer
    ) {

        authService.authorize(request, Permission.POST_CUSTOMER);

        return genService.postCustomer(customer);

    }

    /******************************************************************
     *                                                                *
     * Menu Requests
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.Charge.class)
    @RequestMapping(value = "/charges", method = RequestMethod.GET)
    public List<Charge> getCharges(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_CHARGES);

        return genService.getCharges();

    }

    @JsonView(value = View.CategoryWithMenuItem.class)
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getCategories(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_MENU);

        return genService.getCategories();

    }

    @JsonView(value = View.ModifierGroupWithModifier.class)
    @RequestMapping(value = "/modifierGroups", method = RequestMethod.GET)
    public List<ModifierGroup> getModifierGroups(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_MENU);

        return genService.getModifierGroups();

    }

    /******************************************************************
     *                                                                *
     * Order Requests
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
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

        return oService.findSalesOrders(status, type);

    }

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "/orders/seat", method = RequestMethod.GET)
    public List<SalesOrder> getSalesOrdersBySeat(
            HttpServletRequest request,
            @RequestParam(value = "id", required = true) long id,
            @RequestParam(value = "status", required = false) SalesOrderStatus status
    ) {

        authService.authorize(request, Permission.GET_DINE_IN_SALES_ORDERS);

        return oService.findSalesOrdersBySeat(id, status);

    }

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "/orders/section", method = RequestMethod.GET)
    public List<SalesOrder> getSalesOrderByCustomer(
            HttpServletRequest request,
            @RequestParam(value = "id", required = true) long id,
            @RequestParam(value = "status", required = false) SalesOrderStatus status
    ) {

        authService.authorize(request, Permission.GET_TAKE_OUT_SALES_ORDERS);

        return oService.findSalesOrdersByCustomer(id, status);

    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public long postSalesOrder(
            HttpServletRequest request,
            @RequestBody(required = true) SalesOrder salesOrder
    ) {

        Employee employee = authService.authorize(request);

        if (!employee.isOwnerOf(salesOrder))
            authService.authorize(employee, Permission.ACCESS_ALL_EMPLOYEE_ORDER);

        return oService.processOrder(employee, salesOrder);

    }

    @RequestMapping(value = "/orders/batch", method = RequestMethod.POST)
    public List<Long> postSalesOrders(
            HttpServletRequest request,
            @RequestBody(required = true) List<SalesOrder> salesOrders
    ) {

        Employee employee = authService.authorize(request);

        if (!employee.isOwnerOf(salesOrders))
            authService.authorize(employee, Permission.ACCESS_ALL_EMPLOYEE_ORDER);

        return salesOrders
                .stream()
                .map(salesOrder -> oService.processOrder(employee, salesOrder))
                .collect(Collectors.toList());

    }


}
