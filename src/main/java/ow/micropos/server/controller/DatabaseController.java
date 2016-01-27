package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import email.com.gmail.ttsai0509.print.printer.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.menu.*;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.target.Seat;
import ow.micropos.server.model.target.Section;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.DatabaseService;
import ow.micropos.server.service.PrintService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/database")
public class DatabaseController {

    @Autowired AuthService authService;
    @Autowired DatabaseService dbService;
    @Autowired PrintService printService;

    /******************************************************************
     *                                                                *
     * Printers (Not really a database item)
     *                                                                *
     ******************************************************************/

    public List<String> getPrinters(
            HttpServletRequest request
    ) {
        return printService.getPrinters();
    }


    /******************************************************************
     *                                                                *
     * Positions
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.Position.class)
    @RequestMapping(value = "/positions", method = RequestMethod.GET)
    public List<Position> getPositions(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_POSITIONS);

        return dbService.getPositions();

    }

    @RequestMapping(value = "/positions", method = RequestMethod.POST)
    public long updatePosition(
            HttpServletRequest request,
            @RequestBody(required = true) Position position
    ) {

        authService.authorize(request, Permission.DB_POSITIONS);

        return dbService.updatePosition(position);

    }

    @RequestMapping(value = "/positions/{id}", method = RequestMethod.DELETE)
    public boolean removePosition(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_POSITIONS);

        return dbService.removePosition(id);

    }

    /******************************************************************
     *                                                                *
     * Employees
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.EmployeeWithPosition.class)
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public List<Employee> getEmployees(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_EMPLOYEES);

        return dbService.getEmployees();

    }

    @RequestMapping(value = "/employees", method = RequestMethod.POST)
    public long updateEmployee(
            HttpServletRequest request,
            @RequestBody(required = true) Employee employee
    ) {

        authService.authorize(request, Permission.DB_EMPLOYEES);

        return dbService.updateEmployee(employee);

    }

    @RequestMapping(value = "/employees/{id}", method = RequestMethod.DELETE)
    public boolean removeEmployee(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_EMPLOYEES);

        return dbService.removeEmployee(id);

    }

    /******************************************************************
     *                                                                *
     * Sales Orders
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.SalesOrderAll.class)
    @RequestMapping(value = "/salesOrders", method = RequestMethod.GET)
    public List<SalesOrder> getSalesOrders(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_SALES_ORDERS);

        return dbService.getSalesOrders();

    }

    @RequestMapping(value = "/salesOrders/{id}", method = RequestMethod.DELETE)
    public boolean removeSalesOrder(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_SALES_ORDERS);

        return dbService.removeSalesOrder(id);

    }

    /******************************************************************
     *                                                                *
     * Customers
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.Customer.class)
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<Customer> getCustomers(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_CUSTOMERS);

        return dbService.getCustomers();

    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public long updateCustomers(
            HttpServletRequest request,
            @RequestBody(required = true) Customer customer
    ) {

        authService.authorize(request, Permission.DB_CUSTOMERS);

        return dbService.updateCustomer(customer);

    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    public boolean removeCustomers(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_CUSTOMERS);

        return dbService.removeCustomer(id);

    }

    /******************************************************************
     *                                                                *
     * Menu Items
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.MenuItemWithCategory.class)
    @RequestMapping(value = "/menuItems", method = RequestMethod.GET)
    public List<MenuItem> getMenuItems(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_MENU_ITEMS);

        return dbService.getMenuItems();

    }

    @RequestMapping(value = "/menuItems", method = RequestMethod.POST)
    public long updateMenuItem(
            HttpServletRequest request,
            @RequestBody(required = true) MenuItem menuItem
    ) {

        authService.authorize(request, Permission.DB_MENU_ITEMS);

        return dbService.updateMenuItem(menuItem);

    }

    @RequestMapping(value = "/menuItems/{id}", method = RequestMethod.DELETE)
    public boolean removeMenuItem(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_MENU_ITEMS);

        return dbService.removeMenuItem(id);

    }

    /******************************************************************
     *                                                                *
     * Categories
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.Category.class)
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getCategories(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_CATEGORIES);

        return dbService.getCategories();

    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public long updateCategory(
            HttpServletRequest request,
            @RequestBody(required = true) Category category
    ) {

        authService.authorize(request, Permission.DB_CATEGORIES);

        return dbService.updateCategory(category);

    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    public boolean removeCategory(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_CATEGORIES);

        return dbService.removeCategory(id);

    }

    /******************************************************************
     *                                                                *
     * Modifier Groups
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.ModifierGroup.class)
    @RequestMapping(value = "/modifierGroups", method = RequestMethod.GET)
    public List<ModifierGroup> getModifierGroups(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_MODIFIER_GROUPS);

        return dbService.getModifierGroups();

    }

    @RequestMapping(value = "/modifierGroups", method = RequestMethod.POST)
    public long updateModifierGroup(
            HttpServletRequest request,
            @RequestBody(required = true) ModifierGroup modifierGroup
    ) {

        authService.authorize(request, Permission.DB_MODIFIER_GROUPS);

        return dbService.updateModifierGroup(modifierGroup);

    }

    @RequestMapping(value = "/modifierGroups/{id}", method = RequestMethod.DELETE)
    public boolean removeModifierGroup(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_MODIFIER_GROUPS);

        return dbService.removeModifierGroup(id);

    }

    /******************************************************************
     *                                                                *
     * Modifiers
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.ModifierWithModifierGroup.class)
    @RequestMapping(value = "/modifiers", method = RequestMethod.GET)
    public List<Modifier> getModifiers(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_MODIFIERS);

        return dbService.getModifiers();

    }

    @RequestMapping(value = "/modifiers", method = RequestMethod.POST)
    public long updateModifier(
            HttpServletRequest request,
            @RequestBody(required = true) Modifier modifier
    ) {

        authService.authorize(request, Permission.DB_MODIFIERS);

        return dbService.updateModifier(modifier);

    }

    @RequestMapping(value = "/modifiers/{id}", method = RequestMethod.DELETE)
    public boolean removeModifier(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_MODIFIERS);

        return dbService.removeModifier(id);

    }

    /******************************************************************
     *                                                                *
     * Sections
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.Section.class)
    @RequestMapping(value = "/sections", method = RequestMethod.GET)
    public List<Section> getSections(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_SECTIONS);

        return dbService.getSections();

    }

    @RequestMapping(value = "/sections", method = RequestMethod.POST)
    public long updateSection(
            HttpServletRequest request,
            @RequestBody(required = true) Section section
    ) {

        authService.authorize(request, Permission.DB_SECTIONS);

        return dbService.updateSection(section);

    }

    @RequestMapping(value = "/sections/{id}", method = RequestMethod.DELETE)
    public boolean removeSection(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_SECTIONS);

        return dbService.removeSection(id);

    }

    /******************************************************************
     *                                                                *
     * Seats
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.SeatWithSection.class)
    @RequestMapping(value = "/seats", method = RequestMethod.GET)
    public List<Seat> getSeats(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_SEATS);

        return dbService.getSeats();

    }

    @RequestMapping(value = "/seats", method = RequestMethod.POST)
    public long updateSeat(
            HttpServletRequest request,
            @RequestBody(required = true) Seat seat
    ) {

        authService.authorize(request, Permission.DB_SEATS);

        return dbService.updateSeat(seat);

    }

    @RequestMapping(value = "/seats/{id}", method = RequestMethod.DELETE)
    public boolean removeSeat(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_SEATS);

        return dbService.removeSeat(id);

    }

    /******************************************************************
     *                                                                *
     * Charges
     *                                                                *
     ******************************************************************/

    @JsonView(value = View.Charge.class)
    @RequestMapping(value = "/charges", method = RequestMethod.GET)
    public List<Charge> getCharges(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.DB_CHARGES);

        return dbService.getCharges();

    }

    @RequestMapping(value = "/charges", method = RequestMethod.POST)
    public long updateCharge(
            HttpServletRequest request,
            @RequestBody(required = true) Charge charge
    ) {

        authService.authorize(request, Permission.DB_CHARGES);

        return dbService.updateCharge(charge);

    }

    @RequestMapping(value = "/charges/{id}", method = RequestMethod.DELETE)
    public boolean removeCharge(
            HttpServletRequest request,
            @PathVariable("id") long id
    ) {

        authService.authorize(request, Permission.DB_CHARGES);

        return dbService.removeCharge(id);

    }

}
