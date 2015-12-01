package ow.micropos.server.model;

public final class View {

    private View() {}

    public interface Category {}

    public interface CategoryWithMenuItem extends Category, MenuItem {}

    public interface CategoryAll extends Category, CategoryWithMenuItem {}

    public interface Customer {}

    public interface CustomerWithSalesOrder extends Customer, SalesOrder {}

    public interface CustomerAll extends Customer, CustomerWithSalesOrder {}

    public interface Employee {}

    public interface EmployeeWithSalesOrder extends Employee, SalesOrder {}

    public interface EmployeeWithPosition extends Employee, Position {}

    public interface EmployeeAll extends Employee, EmployeeWithSalesOrder, EmployeeWithPosition {}

    public interface MenuItem {}

    public interface MenuItemWithCategory extends MenuItem, Category {}

    public interface MenuItemWithSalesOrder extends MenuItem, SalesOrder {}

    public interface MenuItemAll extends MenuItem, MenuItemWithCategory, MenuItemWithSalesOrder {}

    public interface Modifier {}

    public interface ModifierWithModifierGroup extends Modifier, ModifierGroup {}

    public interface ModifierWithProductEntry extends Modifier, ProductEntry {}

    public interface ModifierAll extends Modifier, ModifierWithModifierGroup, ModifierWithProductEntry {}

    public interface ModifierGroup {}

    public interface ModifierGroupWithModifier extends ModifierGroup, Modifier {}

    public interface ModifierGroupAll extends ModifierGroup, ModifierGroupWithModifier {}

    public interface SalesOrder {}

    public interface SalesOrderEmployee extends SalesOrder, Employee {}

    public interface SalesOrderTarget extends SalesOrder, Customer, Seat {}

    public interface SalesOrderDetails extends SalesOrder, ProductEntry, PaymentEntry {}

    public interface SalesOrderAll extends SalesOrder, SalesOrderEmployee, SalesOrderTarget, SalesOrderDetails {}

    public interface ProductEntry extends MenuItem, Modifier {}

    public interface ProductEntryWithSalesOrder extends ProductEntry, SalesOrder {}

    public interface ProductEntryAll extends ProductEntry, ProductEntryWithSalesOrder {}

    public interface Seat {}

    public interface SeatWithSection extends Seat, Section {}

    public interface SeatWithSalesOrder extends Seat, SalesOrder {}

    public interface SeatAll extends Seat, SeatWithSection, SeatWithSalesOrder {}

    public interface Section {}

    public interface SectionWithSeat extends Section, Seat {}

    public interface SectionAll extends Section, SectionWithSeat {}

    public interface SectionWithSalesOrder extends SectionWithSeat, SeatWithSalesOrder, SalesOrderEmployee,
            SalesOrderDetails {}

    public interface PaymentEntry {}

    public interface PaymentEntryWithSalesOrder extends PaymentEntry, SalesOrder {}

    public interface PaymentEntryAll extends PaymentEntry, PaymentEntryWithSalesOrder {}

    public interface Menu extends CategoryWithMenuItem, ModifierGroupWithModifier {}

    public interface PrimaryMenu extends Menu {}

    public interface SecondaryMenu extends Menu {}

    public interface MenuAll extends Menu, PrimaryMenu, SecondaryMenu {}

    public interface SalesOrderRecord {}

    public interface SalesOrderRecordEmployee extends SalesOrderRecord, Employee {}

    public interface SalesOrderRecordTarget extends SalesOrderRecord, Customer, Seat {}

    public interface SalesOrderRecordDetails extends SalesOrderRecord, ProductEntryRecord, PaymentEntryRecord {}

    public interface SalesOrderRecordAll extends SalesOrderRecord, SalesOrderRecordEmployee, SalesOrderRecordTarget,
            SalesOrderRecordDetails {}

    public interface PaymentEntryRecord {}

    public interface PaymentEntryRecordWithSalesOrderRecord extends PaymentEntryRecord, SalesOrderRecord {}

    public interface PaymentEntryRecordAll extends PaymentEntryRecord, PaymentEntryRecordWithSalesOrderRecord {}

    public interface ProductEntryRecord extends MenuItem, Modifier {}

    public interface ProductEntryRecordWithSalesOrderRecord extends ProductEntryRecord, SalesOrderRecord {}

    public interface ProductEntryRecordAll extends ProductEntryRecord, ProductEntryRecordWithSalesOrderRecord {}

    public interface Role {}

    public interface RoleWithPosition extends Role, Position {}

    public interface Position {}

    public interface PositionWithEmployee extends Position, Employee {}

    public interface PositionAll extends Position, PositionWithEmployee {}

    public interface SimpleReport {}

    public interface Charge {}

    public interface ChargeAll extends Charge {}

    public interface ChargeEntry {}

    public interface ChargeEntryWithSalesOrder extends ChargeEntry {}

    public interface ChargeWithChargeEntry extends ChargeEntry {}

    public interface ChargeEntryAll extends ChargeEntryWithSalesOrder, ChargeWithChargeEntry {}
}
