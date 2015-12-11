package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.exception.InvalidParameterException;
import ow.micropos.server.exception.ResourceNotFoundException;
import ow.micropos.server.model.menu.Charge;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.ModifierGroup;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.target.Seat;
import ow.micropos.server.model.target.Section;
import ow.micropos.server.repository.menu.ChargeRepository;
import ow.micropos.server.repository.menu.CategoryRepository;
import ow.micropos.server.repository.menu.ModifierGroupRepository;
import ow.micropos.server.repository.target.CustomerRepository;
import ow.micropos.server.repository.employee.EmployeeRepository;
import ow.micropos.server.repository.target.SeatRepository;
import ow.micropos.server.repository.target.SectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralService {

    @Autowired ChargeRepository chargeRepo;
    @Autowired CategoryRepository categoryRepo;
    @Autowired ModifierGroupRepository mgRepo;
    @Autowired EmployeeRepository employeeRepo;
    @Autowired CustomerRepository customerRepo;
    @Autowired SeatRepository seatRepo;
    @Autowired SectionRepository sectionRepo;

    /******************************************************************
     *                                                                *
     * Employee Services
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public Employee getEmployee(short pin) {

        List<Employee> employees = employeeRepo.findByPin(pin);

        if (employees == null)
            throw new InternalServerErrorException("Employee repository error.");

        if (employees.size() > 1)
            throw new InternalServerErrorException("Multiple users with pin.");

        if (employees.size() == 0)
            throw new InvalidParameterException("No employee found.");

        return employees.get(0);

    }

    /******************************************************************
     *                                                                *
     * Customer Services
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Customer> getCustomers(boolean filterArchived, boolean earliestOpen) {

        List<Customer> customers;

        if (filterArchived)
            customers = customerRepo.findByArchived(false);
        else
            customers = customerRepo.findAll();

        if (customers == null)
            throw new InternalServerErrorException("Customer repository error.");

        if (earliestOpen)
            customers.forEach(this::filterEarliestOpen);

        return customers;

    }

    @Transactional(readOnly = true)
    public Customer getCustomer(long id, boolean filterArchived, boolean earliestOpen) {

        Customer customer = customerRepo.findOne(id);

        if (filterArchived && customer.isArchived())
            throw new InternalServerErrorException("Customer archived");

        if (customer == null)
            throw new InternalServerErrorException("Customer repository error.");

        if (earliestOpen)
            filterEarliestOpen(customer);

        return customer;

    }

    @Transactional(readOnly = false)
    public long postCustomer(Customer customer) {

        customer.setId(null);

        return customerRepo.save(customer).getId();

    }

    private void filterEarliestOpen(Customer customer) {
        SalesOrder earliest = customer.getSalesOrders()
                .stream()
                .filter(so -> so.getStatus() == SalesOrderStatus.OPEN)
                .min((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
                .orElse(null);

        List<SalesOrder> earliestAsList = new ArrayList<>();
        if (earliest != null)
            earliestAsList.add(earliest);

        customer.setSalesOrders(earliestAsList);
    }

    /******************************************************************
     *                                                                *
     * Seating Services
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Section> getSections(boolean filterArchived, boolean earliestOpen) {

        List<Section> sections = sectionRepo.findAll();

        if (sections == null)
            throw new InternalServerErrorException("Section repository error.");

        if (filterArchived)
            sections.stream().forEach(this::filterArchived);

        if (earliestOpen)
            sections.forEach(section -> section.getSeats().forEach(this::filterEarliestOpen));

        return sections;

    }

    @Transactional(readOnly = true)
    public Section getSection(long id, boolean filterArchived, boolean earliestOpen) {

        Section section = sectionRepo.getOne(id);

        if (section == null)
            throw new ResourceNotFoundException(id);

        if (filterArchived)
            filterArchived(section);

        if (earliestOpen)
            section.getSeats().forEach(this::filterEarliestOpen);

        return section;

    }

    private void filterArchived(Section section) {
        section.setSeats(section.getSeats()
                .stream()
                .filter(seat -> !seat.isArchived())
                .collect(Collectors.toList()));
    }

    private void filterEarliestOpen(Seat seat) {
        SalesOrder earliest = seat.getSalesOrders()
                .stream()
                .filter(so -> so.getStatus() == SalesOrderStatus.OPEN)
                .min((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
                .orElse(null);

        List<SalesOrder> earliestAsList = new ArrayList<>();
        if (earliest != null)
            earliestAsList.add(earliest);

        seat.setSalesOrders(earliestAsList);
    }

    /******************************************************************
     *                                                                *
     * Menu Services
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Charge> getCharges() {

        List<Charge> charges = chargeRepo.findByArchived(false);

        if (charges == null)
            throw new InternalServerErrorException("Error retrieving charges.");

        return charges;

    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {

        List<Category> categories = categoryRepo.findAll();
        if (categories == null)
            throw new InternalServerErrorException("Error retrieving categories.");

        categories.forEach(this::filterArchivedMenuItems);

        return categories;

    }

    @Transactional(readOnly = true)
    public List<ModifierGroup> getModifierGroups() {

        List<ModifierGroup> modifierGroups = mgRepo.findAll();
        if (modifierGroups == null)
            throw new InternalServerErrorException("Error retrieving modifier groups.");

        modifierGroups.forEach(this::filterArchivedModifiers);

        return modifierGroups;

    }

    private void filterArchivedModifiers(ModifierGroup group) {
        group.setModifiers(
                group.getModifiers()
                        .stream()
                        .filter(modifier -> !modifier.isArchived())
                        .collect(Collectors.toList())
        );
    }

    private void filterArchivedMenuItems(Category category) {
        category.setMenuItems(
                category.getMenuItems()
                        .stream()
                        .filter(menuItem -> !menuItem.isArchived())
                        .collect(Collectors.toList())
        );
    }

}
