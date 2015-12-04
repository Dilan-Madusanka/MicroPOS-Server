package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.people.Customer;
import ow.micropos.server.model.people.Employee;
import ow.micropos.server.model.seating.Section;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.PeopleService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "people")
public class PeopleController {

    @Autowired AuthService authService;
    @Autowired PeopleService pService;

    @JsonView(value = View.EmployeeWithPosition.class)
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public List<Employee> getEmployees(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_EMPLOYEES);

        return pService.getEmployees(false);

    }

    @JsonView(value = View.EmployeeWithPosition.class)
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public Employee getEmployee(
            HttpServletRequest request,
            @RequestParam(value = "pin", required = true) short pin
    ) {

        //authService.authorize(request);

        return pService.getEmployee(pin);

    }

    @JsonView(value = View.CustomerWithSalesOrder.class)
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public List<Customer> getCustomers(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_CUSTOMERS);

        return pService.getCustomers(true, true);

    }

    @JsonView(value = View.CustomerWithSalesOrder.class)
    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    public Customer getCustomer(
            HttpServletRequest request,
            @PathVariable(value = "id") long id
    ) {

        authService.authorize(request, Permission.GET_CUSTOMERS);

        return pService.getCustomer(id, true, true);

    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public long postCustomer(
            HttpServletRequest request,
            @RequestBody(required = true) Customer customer
    ) {

        authService.authorize(request, Permission.POST_CUSTOMER);

        return pService.postCustomer(customer);

    }


}
