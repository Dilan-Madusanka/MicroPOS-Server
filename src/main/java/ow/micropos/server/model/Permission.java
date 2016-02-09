package ow.micropos.server.model;


public enum Permission {

    /******************************************************************
     *                                                                *
     * Client
     *                                                                *
     ******************************************************************/

    CLIENT_CLOSE,
    CLIENT_MANAGER,
    CLIENT_SETTINGS,

    /******************************************************************
     *                                                                *
     * Customers
     *                                                                *
     ******************************************************************/

    GET_CUSTOMERS,
    POST_CUSTOMER,

    /******************************************************************
     *                                                                *
     * Menu
     *                                                                *
     ******************************************************************/

    GET_SECTIONS,
    GET_MENU,
    GET_CHARGES,

    /******************************************************************
     *                                                                *
     * Orders
     *                                                                *
     ******************************************************************/

    CREATE_SALES_ORDER,
    REOPEN_SALES_ORDER,
    CLOSE_SALES_ORDER,
    VOID_SALES_ORDER,

    CREATE_PAYMENT_ENTRY,
    VOID_PAYMENT_ENTRY,

    CREATE_CHARGE_ENTRY,
    VOID_CHARGE_ENTRY,

    CREATE_PRODUCT_ENTRY,
    EDIT_PRODUCT_ENTRY,
    HOLD_PRODUCT_ENTRY,
    VOID_PRODUCT_ENTRY,

    SPLIT_SALES_ORDER,
    ACCESS_ALL_EMPLOYEE_ORDER,

    GET_SALES_ORDERS,
    GET_DINE_IN_SALES_ORDERS,
    GET_TAKE_OUT_SALES_ORDERS,

    /******************************************************************
     *                                                                *
     * Migration
     *                                                                *
     ******************************************************************/

    MIGRATION,

    /******************************************************************
     *                                                                *
     * Reporting
     *                                                                *
     ******************************************************************/

    SALES_REPORT,

    /******************************************************************
     *                                                                *
     * Records
     *                                                                *
     ******************************************************************/

    GET_RECORDS,

    /******************************************************************
     *                                                                *
     * Database
     *                                                                *
     ******************************************************************/

    DB_SALES_ORDERS,
    DB_MENU_ITEMS,
    DB_CATEGORIES,
    DB_MODIFIER_GROUPS,
    DB_MODIFIERS,
    DB_EMPLOYEES,
    DB_POSITIONS,
    DB_SEATS,
    DB_SECTIONS,
    DB_CUSTOMERS,
    DB_CHARGES,

    /******************************************************************
     *                                                                *
     * Time Card Entries
     *                                                                *
     ******************************************************************/

    CLOCK_IN,
    CLOCK_OUT,
    VIEW_TIME_CARD,
    TIME_CARD_CREATE,
    TIME_CARD_UPDATE,
    TIME_CARD_DELETE

}
