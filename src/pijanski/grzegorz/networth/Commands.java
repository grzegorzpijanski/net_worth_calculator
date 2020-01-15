package pijanski.grzegorz.networth;

public abstract class Commands {

    public static final String YES = "y";
    public static final String NO = "n";
    public static final String HELP = "help";
    public static final String HELP_DESC = "Display available commands";
    public static final String EXIT = "exit";
    public static final String EXIT_DESC = "Exit program";
    public static final String CALCULATE_NET_WORTH = "calc";
    public static final String CALCULATE_NET_WORTH_DESC = "Calculate your net worth for this month";
    public static final String CALCULATE_NET_WORTH_MONTH = "calc -m";
    public static final String CALCULATE_NET_WORTH_MONTH_DESC = "Calculate your net worth for given month in format MM:YY";
    public static final String INVOICE_ADD = "i -add";
    public static final String INVOICE_ADD_DESC = "Add an invoice";
    public static final String INVOICE_DELETE = "i -delete";
    public static final String INVOICE_DELETE_DESC = "Delete an invoice";
    public static final String INVOICE_ALL = "i -all";
    public static final String INVOICE_ALL_DESC = "Print all invoices";
    public static final String INVOICE_ALL_MONTH = "i -all -m";
    public static final String INVOICE_ALL_MONTH_DESC = "Print all invoices for a given month in format MM:YY";
    public static final String EXPENSE_ADD = "e -add";
    public static final String EXPENSE_ADD_DESC = "Add an expense";
    public static final String EXPENSE_UPDATE = "e -update";
    public static final String EXPENSE_UPDATE_DESC = "Update an expense";
    public static final String EXPENSE_DELETE = "e -delete";
    public static final String EXPENSE_DELETE_DESC = "Delete an expense";
    public static final String EXPENSE_ALL = "e -all";
    public static final String EXPENSE_ALL_DESC = "Print all expenses";
}
