package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.exception.InvalidParameterException;
import ow.micropos.server.model.people.Customer;
import ow.micropos.server.model.people.Employee;
import ow.micropos.server.repository.people.CustomerRepository;
import ow.micropos.server.repository.people.EmployeeRepository;

import java.util.List;

@Service
public class PeopleService {

    @Autowired EmployeeRepository eRepo;
    @Autowired CustomerRepository cRepo;

    @Transactional(readOnly = true)
    public List<Employee> getEmployees(boolean filterArchived) {

        List<Employee> employees;
        if (filterArchived)
            employees = eRepo.findByArchived(false);
        else
            employees = eRepo.findAll();

        if (employees == null)
            throw new InternalServerErrorException("Employee repository error.");

        return employees;
    }

    @Transactional(readOnly = true)
    public Employee getEmployee(short pin) {

        List<Employee> employees = eRepo.findByPin(pin);

        if (employees == null)
            throw new InternalServerErrorException("Employee repository error.");

        if (employees.size() > 1)
            throw new InternalServerErrorException("Multiple users with pin.");

        if (employees.size() == 0)
            throw new InvalidParameterException("No employee found.");

        return employees.get(0);

    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomers(boolean filterArchived) {

        List<Customer> customers;
        if (filterArchived)
            customers = cRepo.findByArchived(false);
        else
            customers = cRepo.findAll();

        if (customers == null)
            throw new InternalServerErrorException("Customer repository error.");

        return customers;


    }

    @Transactional(readOnly = false)
    public long postCustomer(Customer customer) {

        customer.setId(null);

        return cRepo.save(customer).getId();

    }

}
