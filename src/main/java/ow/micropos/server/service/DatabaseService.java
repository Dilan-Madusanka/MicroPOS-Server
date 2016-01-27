package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.MicroPosException;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.menu.*;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.target.Customer;
import ow.micropos.server.model.target.Seat;
import ow.micropos.server.model.target.Section;
import ow.micropos.server.repository.auth.PositionRepository;
import ow.micropos.server.repository.employee.EmployeeRepository;
import ow.micropos.server.repository.menu.*;
import ow.micropos.server.repository.orders.ChargeEntryRepository;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;
import ow.micropos.server.repository.records.ChargeEntryRecordRepository;
import ow.micropos.server.repository.records.PaymentEntryRecordRepository;
import ow.micropos.server.repository.records.ProductEntryRecordRepository;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;
import ow.micropos.server.repository.target.CustomerRepository;
import ow.micropos.server.repository.target.SeatRepository;
import ow.micropos.server.repository.target.SectionRepository;

import java.util.Date;
import java.util.List;

@Service
public class DatabaseService {

    @Autowired SalesOrderRepository soRepo;
    @Autowired ProductEntryRepository prodRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired ChargeEntryRepository chRepo;

    @Autowired SalesOrderRecordRepository sorRepo;
    @Autowired ProductEntryRecordRepository prodrRepo;
    @Autowired PaymentEntryRecordRepository payrRepo;
    @Autowired ChargeEntryRecordRepository chrRepo;

    @Autowired CategoryRepository categoryRepo;
    @Autowired MenuItemRepository miRepo;
    @Autowired ModifierGroupRepository mgRepo;
    @Autowired ModifierRepository modifierRepo;
    @Autowired SectionRepository sectionRepo;
    @Autowired SeatRepository seatRepo;
    @Autowired ChargeRepository chargeRepo;

    @Autowired CustomerRepository customerRepo;
    @Autowired EmployeeRepository employeeRepo;
    @Autowired PositionRepository positionRepo;


    /******************************************************************
     *                                                                *
     * Position Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Position> getPositions() {
        return positionRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updatePosition(Position position) {

        if (position.getId() == null) {

            position.setDate(new Date());
            positionRepo.save(position);

        } else {

            positionRepo.save(position);

        }

        return position.getId();

    }

    @Transactional(readOnly = false)
    public boolean removePosition(long id) {

        Position oldPosition = positionRepo.getOne(id);

        if (oldPosition != null) {

            // Remove this position from each employee.
            List<Employee> employees = oldPosition.getEmployees();

            if (employees != null && !employees.isEmpty()) {
                employees.forEach(employee -> employee.getPositions().remove(oldPosition));
                employeeRepo.save(employees);
            }

            positionRepo.delete(id);

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Employee Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Employee> getEmployees() {
        return employeeRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateEmployee(Employee employee) {

        List<Employee> employees = employeeRepo.findByPin(employee.getPin());

        if (employees != null && !employees.isEmpty())
            for (Employee emp : employees)
                if (!emp.getId().equals(employee.getId()))
                    throw new MicroPosException("Employee with that PIN exists");

        if (employee.getId() == null) {
            employee.setDate(new Date());
            employeeRepo.save(employee);

        } else {
            employeeRepo.save(employee);
        }

        return employee.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeEmployee(long id) {

        Employee oldEmployee = employeeRepo.findOne(id);

        if (oldEmployee != null) {

            // Referenced employees must be archived
            if (oldEmployee.getSalesOrders() != null && !oldEmployee.getSalesOrders().isEmpty()) {
                oldEmployee.setArchived(true);
                oldEmployee.setArchiveDate(new Date());
                employeeRepo.save(oldEmployee);
            } else if (oldEmployee.getSalesOrderRecords() != null && !oldEmployee.getSalesOrderRecords().isEmpty()) {
                oldEmployee.setArchived(true);
                oldEmployee.setArchiveDate(new Date());
                employeeRepo.save(oldEmployee);

                // Unreferenced employees can be deleted
            } else {
                employeeRepo.delete(id);
            }

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Sales Order Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<SalesOrder> getSalesOrders() {
        return soRepo.findAll();
    }

    @Transactional(readOnly = false)
    public boolean removeSalesOrder(long id) {

        SalesOrder so = soRepo.findOne(id);

        if (so == null)
            return false;

        so.getProductEntries().forEach(prodRepo::delete);
        so.getPaymentEntries().forEach(payRepo::delete);
        so.getChargeEntries().forEach(chRepo::delete);
        soRepo.delete(so);

        return true;
    }

    /******************************************************************
     *                                                                *
     * Customer Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Customer> getCustomers() {
        return customerRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateCustomer(Customer customer) {

        if (customer.getId() == null) {

            customer.setDate(new Date());
            customerRepo.save(customer);

        } else {

            customerRepo.save(customer);

        }

        return customer.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeCustomer(long id) {

        Customer oldCustomer = customerRepo.findOne(id);

        if (oldCustomer != null) {

            // Referenced customers must be archived
            if (oldCustomer.getSalesOrders() != null && !oldCustomer.getSalesOrders().isEmpty()) {
                oldCustomer.setArchived(true);
                oldCustomer.setArchiveDate(new Date());
                customerRepo.save(oldCustomer);
            } else if (oldCustomer.getSalesOrderRecords() != null && !oldCustomer.getSalesOrderRecords().isEmpty()) {
                oldCustomer.setArchived(true);
                oldCustomer.setArchiveDate(new Date());
                customerRepo.save(oldCustomer);

                // Unreferenced customers can be deleted
            } else {
                customerRepo.delete(id);
            }

            return true;

        } else {

            return false;

        }
    }


    /******************************************************************
     *                                                                *
     * Menu Item Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<MenuItem> getMenuItems() {
        return miRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateMenuItem(MenuItem menuItem) {

        if (menuItem.getId() == null) {

            menuItem.setDate(new Date());
            miRepo.save(menuItem);

        } else {

            MenuItem oldMenuItem = miRepo.findOne(menuItem.getId());

            // Referenced menu items prices can not be updated in place.
            if (oldMenuItem.getProductEntries() != null
                    && !oldMenuItem.getProductEntries().isEmpty()
                    && oldMenuItem.getPrice().compareTo(menuItem.getPrice()) != 0) {
                oldMenuItem.setArchived(true);
                oldMenuItem.setArchiveDate(new Date());
                miRepo.save(oldMenuItem);
                menuItem.setId(null);
                miRepo.save(menuItem);
            } else if (oldMenuItem.getProductEntryRecords() != null
                    && !oldMenuItem.getProductEntryRecords().isEmpty()
                    && oldMenuItem.getPrice().compareTo(menuItem.getPrice()) != 0) {
                oldMenuItem.setArchived(true);
                oldMenuItem.setArchiveDate(new Date());
                miRepo.save(oldMenuItem);
                menuItem.setId(null);
                miRepo.save(menuItem);

                // Unreferenced menu items can be updated in place
            } else {
                miRepo.save(menuItem);
            }

        }

        return menuItem.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeMenuItem(long id) {

        MenuItem oldMenuItem = miRepo.findOne(id);

        if (oldMenuItem != null) {

            // Referenced menu items must be archived
            if (oldMenuItem.getProductEntries() != null
                    && !oldMenuItem.getProductEntries().isEmpty()) {
                oldMenuItem.setArchived(true);
                oldMenuItem.setArchiveDate(new Date());
                miRepo.save(oldMenuItem);
            } else if (oldMenuItem.getProductEntryRecords() != null
                    && !oldMenuItem.getProductEntryRecords().isEmpty()) {
                oldMenuItem.setArchived(true);
                oldMenuItem.setArchiveDate(new Date());
                miRepo.save(oldMenuItem);

                // Unreferenced menu items can be deleted
            } else {
                miRepo.delete(id);
            }

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Category Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateCategory(Category category) {

        if (category.getId() == null) {

            category.setDate(new Date());
            categoryRepo.save(category);

        } else {

            // All updates can be performed.
            categoryRepo.save(category);

        }

        return category.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeCategory(long id) {

        Category oldCategory = categoryRepo.findOne(id);

        if (oldCategory != null) {

            // Unreferenced categories can be deleted
            if (oldCategory.getMenuItems() == null
                    || oldCategory.getMenuItems().isEmpty()) {
                categoryRepo.delete(id);

                // Referenced categories must be archived
            } else {
                oldCategory.setArchived(true);
                oldCategory.setArchiveDate(new Date());
                categoryRepo.save(oldCategory);
            }

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Modifier Group Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<ModifierGroup> getModifierGroups() {
        return mgRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateModifierGroup(ModifierGroup modifierGroup) {

        if (modifierGroup.getId() == null) {

            modifierGroup.setDate(new Date());
            mgRepo.save(modifierGroup);

        } else {

            // All updates can be performed.
            mgRepo.save(modifierGroup);

        }

        return modifierGroup.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeModifierGroup(long id) {

        ModifierGroup oldModifierGroup = mgRepo.findOne(id);

        if (oldModifierGroup != null) {

            // Unreferenced modifier groups can be deleted
            if (oldModifierGroup.getModifiers() == null
                    || oldModifierGroup.getModifiers().isEmpty()) {
                mgRepo.delete(id);

                // Referenced categories must be archived
            } else {
                oldModifierGroup.setArchived(true);
                oldModifierGroup.setArchiveDate(new Date());
                mgRepo.save(oldModifierGroup);
            }

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Modifier Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Modifier> getModifiers() {
        return modifierRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateModifier(Modifier modifier) {

        if (modifier.getId() == null) {

            modifier.setDate(new Date());
            modifierRepo.save(modifier);

        } else {

            Modifier oldModifier = modifierRepo.findOne(modifier.getId());

            // Referenced modifier prices can not be updated in place.
            if (oldModifier.getProductEntries() != null
                    && !oldModifier.getProductEntries().isEmpty()
                    && oldModifier.getPrice().compareTo(modifier.getPrice()) != 0) {
                oldModifier.setArchived(true);
                oldModifier.setArchiveDate(new Date());
                modifierRepo.save(oldModifier);
                modifier.setId(null);
                modifierRepo.save(modifier);
            } else if (oldModifier.getProductEntryRecords() != null
                    && !oldModifier.getProductEntryRecords().isEmpty()
                    && oldModifier.getPrice().compareTo(modifier.getPrice()) != 0) {
                oldModifier.setArchived(true);
                oldModifier.setArchiveDate(new Date());
                modifierRepo.save(oldModifier);
                modifier.setId(null);
                modifierRepo.save(modifier);

                // Unreferenced modifiers can be updated in place
            } else {
                modifierRepo.save(modifier);
            }
        }

        return modifier.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeModifier(long id) {

        Modifier oldModifier = modifierRepo.findOne(id);

        if (oldModifier != null) {

            // Referenced modifiers must be archived
            if (oldModifier.getProductEntries() != null
                    && !oldModifier.getProductEntries().isEmpty()) {
                oldModifier.setArchived(true);
                oldModifier.setArchiveDate(new Date());
                modifierRepo.save(oldModifier);
            } else if (oldModifier.getProductEntryRecords() != null
                    && !oldModifier.getProductEntryRecords().isEmpty()) {
                oldModifier.setArchived(true);
                oldModifier.setArchiveDate(new Date());
                modifierRepo.save(oldModifier);

                // Unreferenced modifiers can be deleted
            } else {
                modifierRepo.delete(id);
            }

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Charge Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Charge> getCharges() {
        return chargeRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateCharge(Charge charge) {

        if (charge.getId() == null) {

            charge.setDate(new Date());
            chargeRepo.save(charge);

        } else {

            Charge oldCharge = chargeRepo.findOne(charge.getId());

            // Referenced charge amounts & types can not be updated in place.
            if ((oldCharge.getAmount().compareTo(charge.getAmount()) != 0 || oldCharge.getType() != charge.getType())
                    && oldCharge.getChargeEntries() != null
                    && !oldCharge.getChargeEntries().isEmpty()) {
                oldCharge.setArchived(true);
                oldCharge.setArchiveDate(new Date());
                chargeRepo.save(oldCharge);
                charge.setId(null);
                chargeRepo.save(charge);
            } else if ((oldCharge.getAmount().compareTo(charge.getAmount()) != 0 || oldCharge.getType() != charge
                    .getType())
                    && oldCharge.getChargeEntryRecords() != null
                    && !oldCharge.getChargeEntryRecords().isEmpty()) {
                oldCharge.setArchived(true);
                oldCharge.setArchiveDate(new Date());
                chargeRepo.save(oldCharge);
                charge.setId(null);
                chargeRepo.save(charge);

                // Unreferenced charges can be updated in place
            } else {
                chargeRepo.save(charge);
            }
        }

        return charge.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeCharge(long id) {

        Charge oldCharge = chargeRepo.findOne(id);

        if (oldCharge != null) {

            // Referenced charges must be archived
            if (oldCharge.getChargeEntries() != null
                    && !oldCharge.getChargeEntries().isEmpty()) {
                oldCharge.setArchived(true);
                oldCharge.setArchiveDate(new Date());
                chargeRepo.save(oldCharge);
            } else if (oldCharge.getChargeEntryRecords() != null
                    && !oldCharge.getChargeEntryRecords().isEmpty()) {
                oldCharge.setArchived(true);
                oldCharge.setArchiveDate(new Date());
                chargeRepo.save(oldCharge);

                // Unreferenced charges can be deleted
            } else {
                chargeRepo.delete(id);
            }

            return true;

        } else {

            return false;

        }
    }


    /******************************************************************
     *                                                                *
     * Section Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Section> getSections() {
        return sectionRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateSection(Section section) {

        if (section.getId() == null) {

            section.setDate(new Date());
            sectionRepo.save(section);

        } else {

            // All updates can be performed.
            sectionRepo.save(section);

        }

        return section.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeSection(long id) {

        Section oldSection = sectionRepo.findOne(id);

        if (oldSection != null) {

            // Unreferenced categories can be deleted
            if (oldSection.getSeats() == null
                    || oldSection.getSeats().isEmpty()) {
                sectionRepo.delete(id);

                // Referenced categories must be archived
            } else {
                oldSection.setArchived(true);
                oldSection.setArchiveDate(new Date());
                sectionRepo.save(oldSection);
            }

            return true;

        } else {

            return false;

        }
    }

    /******************************************************************
     *                                                                *
     * Seat Management
     *                                                                *
     ******************************************************************/

    @Transactional(readOnly = true)
    public List<Seat> getSeats() {
        return seatRepo.findByArchived(false);
    }

    @Transactional(readOnly = false)
    public long updateSeat(Seat seat) {

        if (seat.getId() == null) {

            seat.setDate(new Date());
            seatRepo.save(seat);

        } else {

            // All updates can be performed.
            seatRepo.save(seat);

        }

        return seat.getId();

    }

    @Transactional(readOnly = false)
    public boolean removeSeat(long id) {

        Seat oldSeat = seatRepo.findOne(id);

        if (oldSeat != null) {

            // Referenced seats must be archived
            if (oldSeat.getSalesOrders() != null && !oldSeat.getSalesOrders().isEmpty()) {
                oldSeat.setArchived(true);
                oldSeat.setArchiveDate(new Date());
                seatRepo.save(oldSeat);
            } else if (oldSeat.getSalesOrderRecords() != null && !oldSeat.getSalesOrderRecords().isEmpty()) {
                oldSeat.setArchived(true);
                oldSeat.setArchiveDate(new Date());
                seatRepo.save(oldSeat);

                // Unreferenced seats can be deleted
            } else {
                seatRepo.delete(id);
            }

            return true;

        } else {

            return false;

        }
    }

}