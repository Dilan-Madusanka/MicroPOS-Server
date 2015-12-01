package ow.micropos.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.charge.Charge;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.MenuItem;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.menu.ModifierGroup;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.people.Customer;
import ow.micropos.server.model.people.Employee;
import ow.micropos.server.model.seating.Seat;
import ow.micropos.server.model.seating.Section;
import ow.micropos.server.repository.auth.PositionRepository;
import ow.micropos.server.repository.charge.ChargeRepository;
import ow.micropos.server.repository.menu.CategoryRepository;
import ow.micropos.server.repository.menu.MenuItemRepository;
import ow.micropos.server.repository.menu.ModifierGroupRepository;
import ow.micropos.server.repository.menu.ModifierRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.people.CustomerRepository;
import ow.micropos.server.repository.people.EmployeeRepository;
import ow.micropos.server.repository.seating.SeatRepository;
import ow.micropos.server.repository.seating.SectionRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class Initializer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private TypeReference<List<Charge>> chargeListType = new TypeReference<List<Charge>>() {};
    private TypeReference<List<Position>> positionListType = new TypeReference<List<Position>>() {};
    private TypeReference<List<Customer>> customerListType = new TypeReference<List<Customer>>() {};
    private TypeReference<List<Category>> categoryListType = new TypeReference<List<Category>>() {};
    private TypeReference<List<Modifier>> modifierListType = new TypeReference<List<Modifier>>() {};
    private TypeReference<List<MenuItem>> menuItemListType = new TypeReference<List<MenuItem>>() {};
    private TypeReference<List<Section>> sectionListType = new TypeReference<List<Section>>() {};
    private TypeReference<List<Seat>> seatListType = new TypeReference<List<Seat>>() {};
    private TypeReference<List<Employee>> employeeListType = new TypeReference<List<Employee>>() {};
    private TypeReference<List<ModifierGroup>> modifierGroupListType = new TypeReference<List<ModifierGroup>>() {};
    private TypeReference<List<SalesOrder>> salesOrderListType = new TypeReference<List<SalesOrder>>() {};

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ChargeRepository chargeRepo;
    @Autowired
    private PositionRepository positionRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ModifierRepository modifierRepo;
    @Autowired
    private MenuItemRepository menuItemRepo;
    @Autowired
    private SectionRepository sectionRepo;
    @Autowired
    private SeatRepository seatRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private ModifierGroupRepository modifierGroupRepo;
    @Autowired
    private SalesOrderRepository salesOrderRepo;

    @Value("charges.json")
    private ClassPathResource chargesResource;
    @Value("positions.json")
    private ClassPathResource positionsResource;
    @Value("customers.json")
    private ClassPathResource customersResource;
    @Value("categories.json")
    private ClassPathResource categoriesResource;
    @Value("modifiers.json")
    private ClassPathResource modifiersResource;
    @Value("menuitems.json")
    private ClassPathResource menuItemsResource;
    @Value("sections.json")
    private ClassPathResource sectionsResource;
    @Value("seats.json")
    private ClassPathResource seatsResource;
    @Value("employees.json")
    private ClassPathResource employeesResource;
    @Value("modifiergroups.json")
    private ClassPathResource modifierGroupsResource;
    @Value("salesorders.json")
    private ClassPathResource salesOrdersResource;

    @Transactional
    public void init() {

        logger.warn("ATTEMPTING TO RESET DATABASE");

        reset(chargeRepo, chargesResource, chargeListType);

        reset(positionRepo, positionsResource, positionListType);
        reset(employeeRepo, employeesResource, employeeListType);

        reset(customerRepo, customersResource, customerListType);

        reset(sectionRepo, sectionsResource, sectionListType);
        reset(seatRepo, seatsResource, seatListType);

        reset(categoryRepo, categoriesResource, categoryListType);
        reset(menuItemRepo, menuItemsResource, menuItemListType);

        reset(modifierGroupRepo, modifierGroupsResource, modifierGroupListType);
        reset(modifierRepo, modifiersResource, modifierListType);

    }

    private <T> boolean reset(JpaRepository<T, Long> repo, ClassPathResource resource, TypeReference<List<T>>
            listTypeRef) {
        try {
            List<T> items = mapper.readValue(resource.getFile(), listTypeRef);
            return repo.save(items) != null;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

}
