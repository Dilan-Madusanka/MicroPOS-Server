package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ow.micropos.server.repository.target.CustomerRepository;
import ow.micropos.server.repository.target.SeatRepository;
import ow.micropos.server.repository.target.SectionRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DatabaseService {

    @Autowired SalesOrderRepository soRepo;
    @Autowired ProductEntryRepository prodRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired ChargeEntryRepository chRepo;

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

            // Unreferenced customers can be deleted
            if (oldCustomer.getSalesOrders() == null
                    || oldCustomer.getSalesOrders().isEmpty()) {
                customerRepo.delete(id);

                // Referenced customers must be archived
            } else {
                oldCustomer.setArchived(true);
                oldCustomer.setArchiveDate(new Date());
                customerRepo.save(oldCustomer);
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

            // Unreferenced menu items can be updated in place.
            if (oldMenuItem.getProductEntries() == null || oldMenuItem.getProductEntries().isEmpty()) {
                miRepo.save(menuItem);

                // Referenced menu items prices can not be updated in place.
            } else if (oldMenuItem.getPrice().compareTo(menuItem.getPrice()) != 0) {
                oldMenuItem.setArchived(true);
                oldMenuItem.setArchiveDate(new Date());
                miRepo.save(oldMenuItem);

                menuItem.setId(null);
                miRepo.save(menuItem);

                // Referenced menu items cosmetic changed can be updated in place.
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

            // Unreferenced menu items can be deleted
            if (oldMenuItem.getProductEntries() == null
                    || oldMenuItem.getProductEntries().isEmpty()) {
                miRepo.delete(id);

                // Referenced menu items must be archived
            } else {
                oldMenuItem.setArchived(true);
                oldMenuItem.setArchiveDate(new Date());
                miRepo.save(oldMenuItem);
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

            // Unreferenced menu items can be updated in place.
            if (oldModifier.getProductEntries() == null || oldModifier.getProductEntries().isEmpty()) {
                modifierRepo.save(modifier);

                // Referenced menu items prices can not be updated in place.
            } else if (oldModifier.getPrice().compareTo(modifier.getPrice()) != 0) {
                oldModifier.setArchived(true);
                oldModifier.setArchiveDate(new Date());
                modifierRepo.save(oldModifier);

                modifier.setId(null);
                modifierRepo.save(modifier);

                // Referenced menu items cosmetic changed can be updated in place.
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

            // Unreferenced menu items can be deleted
            if (oldModifier.getProductEntries() == null
                    || oldModifier.getProductEntries().isEmpty()) {
                modifierRepo.delete(id);

                // Referenced menu items must be archived
            } else {
                oldModifier.setArchived(true);
                oldModifier.setArchiveDate(new Date());
                modifierRepo.save(oldModifier);
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

            // Unreferenced menu items can be updated in place.
            if (oldCharge.getChargeEntries() == null || oldCharge.getChargeEntries().isEmpty()) {
                chargeRepo.save(charge);

                // Referenced menu items prices can not be updated in place.
            } else if (oldCharge.getAmount().compareTo(charge.getAmount()) != 0
                    || oldCharge.getType() != charge.getType()) {
                oldCharge.setArchived(true);
                oldCharge.setArchiveDate(new Date());
                chargeRepo.save(oldCharge);

                charge.setId(null);
                chargeRepo.save(charge);

                // Referenced menu items cosmetic changed can be updated in place.
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

            // Unreferenced menu items can be deleted
            if (oldCharge.getChargeEntries() == null
                    || oldCharge.getChargeEntries().isEmpty()) {
                chargeRepo.delete(id);

                // Referenced menu items must be archived
            } else {
                oldCharge.setArchived(true);
                oldCharge.setArchiveDate(new Date());
                chargeRepo.save(oldCharge);
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

            // Unreferenced categories can be deleted
            if (oldSeat.getSalesOrders() == null
                    || oldSeat.getSalesOrders().isEmpty()) {
                seatRepo.delete(id);

                // Referenced categories must be archived
            } else {
                oldSeat.setArchived(true);
                oldSeat.setArchiveDate(new Date());
                seatRepo.save(oldSeat);
            }

            return true;

        } else {

            return false;

        }
    }

}