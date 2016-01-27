package ow.micropos.server.model;

//@formatter:off
public final class View {

    private View() {}

    /******************************************************************
     *                                                                *
     * Menu
     *                                                                *
     ******************************************************************/

    public interface Menu extends CategoryWithMenuItem, ModifierGroupWithModifier {}

    public interface Category {}
    public interface CategoryWithMenuItem extends Category, MenuItem {}
    public interface CategoryAll extends Category, CategoryWithMenuItem {}

    public interface MenuItem {}
    public interface MenuItemWithCategory extends MenuItem, Category {}
    public interface MenuItemWithSalesOrder extends MenuItem, SalesOrder {}
    public interface MenuItemWithSalesOrderRecord extends MenuItem, SalesOrderRecord {}
    public interface MenuItemAll extends
            MenuItem,
            MenuItemWithCategory,
            MenuItemWithSalesOrder,
            MenuItemWithSalesOrderRecord {}

    public interface Modifier {}
    public interface ModifierWithModifierGroup extends Modifier, ModifierGroup {}
    public interface ModifierWithProductEntry extends Modifier, ProductEntry {}
    public interface ModifierWithProductEntryRecord extends Modifier, ProductEntryRecord {}
    public interface ModifierAll extends
            Modifier,
            ModifierWithModifierGroup,
            ModifierWithProductEntry,
            ModifierWithProductEntryRecord {}

    public interface ModifierGroup {}
    public interface ModifierGroupWithModifier extends ModifierGroup, Modifier {}
    public interface ModifierGroupAll extends ModifierGroup, ModifierGroupWithModifier {}

    /******************************************************************
     *                                                                *
     * Charges
     *                                                                *
     ******************************************************************/

    public interface Charge {}
    public interface ChargeWithChargeEntry extends Charge, ChargeEntry {}
    public interface ChargeWithChargeEntryRecord extends Charge, ChargeEntryRecord {}
    public interface ChargeAll extends Charge, ChargeWithChargeEntry, ChargeWithChargeEntryRecord {}

    /******************************************************************
     *                                                                *
     * Seating
     *                                                                *
     ******************************************************************/

    public interface Seat {}
    public interface SeatWithSection extends Seat, Section {}
    public interface SeatWithSalesOrder extends Seat, SalesOrder {}
    public interface SeatWithSalesOrderRecord extends Seat, SalesOrderRecord {}
    public interface SeatAll extends Seat, SeatWithSection, SeatWithSalesOrder, SeatWithSalesOrderRecord {}

    public interface Section {}
    public interface SectionWithSeat extends Section, Seat {}
    public interface SectionAll extends Section, SectionWithSeat {}
    public interface SectionWithSalesOrder extends
            SectionWithSeat,
            SeatWithSalesOrder,
            SalesOrderEmployee,
            SalesOrderDetails {}

    /******************************************************************
     *                                                                *
     * People
     *                                                                *
     ******************************************************************/

    public interface Customer {}
    public interface CustomerWithSalesOrder extends Customer, SalesOrder {}
    public interface CustomerWithSalesOrderRecord extends Customer, SalesOrderRecord {}
    public interface CustomerAll extends Customer, CustomerWithSalesOrder, CustomerWithSalesOrderRecord {}

    public interface Employee {}
    public interface EmployeeWithSalesOrder extends Employee, SalesOrder {}
    public interface EmployeeWithSalesOrderRecord extends Employee, SalesOrderRecord {}
    public interface EmployeeWithPosition extends Employee, Position {}
    public interface EmployeeAll extends
            Employee,
            EmployeeWithSalesOrder,
            EmployeeWithSalesOrderRecord,
            EmployeeWithPosition {}

    /******************************************************************
     *                                                                *
     * Authorization
     *                                                                *
     ******************************************************************/

    public interface Role {}
    public interface RoleWithPosition extends Role, Position {}

    public interface Position {}
    public interface PositionWithEmployee extends Position, Employee {}
    public interface PositionAll extends Position, PositionWithEmployee {}

    /******************************************************************
     *                                                                *
     * Order
     *                                                                *
     ******************************************************************/

    public interface ChargeEntry extends Charge {}
    public interface ChargeEntryWithSalesOrder extends ChargeEntry, SalesOrder {}
    public interface ChargeEntryAll extends ChargeEntry, ChargeEntryWithSalesOrder {}

    public interface PaymentEntry {}
    public interface PaymentEntryWithSalesOrder extends PaymentEntry, SalesOrder {}
    public interface PaymentEntryAll extends PaymentEntry, PaymentEntryWithSalesOrder {}

    public interface ProductEntry extends MenuItem, Modifier {}
    public interface ProductEntryWithSalesOrder extends ProductEntry, SalesOrder {}
    public interface ProductEntryAll extends ProductEntry, ProductEntryWithSalesOrder {}

    public interface SalesOrder {}
    public interface SalesOrderEmployee extends SalesOrder, Employee {}
    public interface SalesOrderTarget extends SalesOrder, Customer, Seat {}
    public interface SalesOrderDetails extends SalesOrder, ProductEntry, PaymentEntry, ChargeEntry {}
    public interface SalesOrderAll extends SalesOrder, SalesOrderEmployee, SalesOrderTarget, SalesOrderDetails {}

    /******************************************************************
     *                                                                *
     * Order Records
     *                                                                *
     ******************************************************************/
    
    public interface ChargeEntryRecord extends Charge {}
    public interface ChargeEntryRecordWithSalesOrderRecord extends ChargeEntryRecord, SalesOrderRecord {}
    public interface ChargeEntryRecordAll extends ChargeEntryRecord, ChargeEntryRecordWithSalesOrderRecord {}
    
    public interface PaymentEntryRecord {}
    public interface PaymentEntryRecordWithSalesOrderRecord extends PaymentEntryRecord, SalesOrderRecord {}
    public interface PaymentEntryRecordAll extends PaymentEntryRecord, PaymentEntryRecordWithSalesOrderRecord {}
    
    public interface ProductEntryRecord extends MenuItem, Modifier {}
    public interface ProductEntryRecordWithSalesOrderRecord extends ProductEntryRecord, SalesOrderRecord {}
    public interface ProductEntryRecordAll extends ProductEntryRecord, ProductEntryRecordWithSalesOrderRecord {}

    public interface SalesOrderRecord {}
    public interface SalesOrderRecordEmployee extends SalesOrderRecord, Employee {}
    public interface SalesOrderRecordTarget extends SalesOrderRecord, Customer, Seat {}
    public interface SalesOrderRecordDetails extends
            SalesOrderRecord,
            ProductEntryRecord,
            PaymentEntryRecord,
            ChargeEntryRecord {}
    public interface SalesOrderRecordAll extends
            SalesOrderRecord,
            SalesOrderRecordEmployee,
            SalesOrderRecordTarget,
            SalesOrderRecordDetails {}

    /******************************************************************
     *                                                                *
     * Reporting
     *                                                                *
     ******************************************************************/

    public interface SimpleReport {}

}
//@formatter:on
