package pijanski.grzegorz.networth;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static pijanski.grzegorz.networth.Commands.*;

public class Main {

    private static final String URL = "jdbc:sqlite:db/net_worth_calculator_db";

    public static ExpenseDao expenseDao;
    public static InvoiceDao invoiceDao;

    public static void main(String[] args) {
    	final var dbConnector = DbConnector.getInstance();
	    Connection connection = null;

	    try {
			connection = dbConnector.connect(URL);
		} catch (final SQLException e) {
	    	System.out.println(e.getMessage());
		}
	    
	    if (connection == null) {
	    	System.exit(0);
		}

	    expenseDao = new ExpenseDao(connection);
	    invoiceDao = new InvoiceDao(connection);

	    interact();
    }

    public static void interact() {
		System.out.println("Welcome to your net worth calculator.\nFor help please type: help");

		final var scanner = new Scanner(System.in);

		try {
			while (true) {
				switch (scanner.nextLine()) {
					case HELP: System.out.println("Available commands:\n" +
							String.format("%s : %s\n", HELP, HELP_DESC) +
							String.format("%s : %s\n", EXIT, EXIT_DESC) +
							String.format("%s : %s\n", CALCULATE_NET_WORTH, CALCULATE_NET_WORTH_DESC) +
							String.format("%s : %s\n", CALCULATE_NET_WORTH_MONTH, CALCULATE_NET_WORTH_MONTH_DESC) +
							String.format("%s : %s\n", INVOICE_ADD, INVOICE_ADD_DESC) +
							String.format("%s : %s\n", INVOICE_DELETE, INVOICE_DELETE_DESC) +
							String.format("%s : %s\n", INVOICE_ALL, INVOICE_ALL_DESC) +
							String.format("%s : %s\n", INVOICE_ALL_MONTH, INVOICE_ALL_MONTH_DESC) +
							String.format("%s : %s\n", EXPENSE_ADD, EXPENSE_ADD_DESC) +
							String.format("%s : %s\n", EXPENSE_UPDATE, EXPENSE_UPDATE_DESC) +
							String.format("%s : %s\n", EXPENSE_DELETE, EXPENSE_DELETE_DESC) +
							String.format("%s : %s\n", EXPENSE_ALL, EXPENSE_ALL_DESC));
						break;
					case EXIT: {
						System.out.println("Good bye");
						System.exit(0);
					}
					case CALCULATE_NET_WORTH: {
						try {
							var invoices = invoiceDao.getInvoices(LocalDate.now());
							var expenses = expenseDao.getExpenses();
							final var netWorth = NetWorthCalculator.calculate(invoices, expenses);
							System.out.println(String.format("Your net worth this month is equal %.2f", netWorth));
						} catch (final SQLException e) {
							System.out.println(e.getMessage());
						}
						break;
					}
					case CALCULATE_NET_WORTH_MONTH: {
						try {
							System.out.println("Please provide the year in format yyyy");
							var year = scanner.nextLine();
							System.out.println("Please provide the month in format MM");
							var month = scanner.nextLine();
							if (month.startsWith("0")) {
								month = month.substring(1);
							}
							var date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
							var invoices = invoiceDao.getInvoices(date);
							var expenses = expenseDao.getExpenses();
							final var netWorth = NetWorthCalculator.calculate(invoices, expenses);
							System.out.println(String.format("Your net worth in %s-%s is equal %.2f", month, year, netWorth));
							scanner.nextLine();
						} catch (final SQLException e) {
							e.getMessage();
						}
					}
					case INVOICE_ADD: {
						System.out.println("Please provide the name of invoice");
						var name = scanner.nextLine();
						System.out.println("Please provide the rate per hour");
						var rate = scanner.nextDouble();
						System.out.println("Please provide the number of hours of invoice");
						var hours = scanner.nextDouble();
						System.out.println("Additional parameters are: date(default: actual) and VAT(default: true). Would you like to custom them? Answer with y/n");
						var next = scanner.next();
						switch (String.valueOf(next)) {
							case YES: {
								LocalDate date;
								System.out.println("Please provide the date of invoice in format yyyy-MM-dd");
								try {
									date = LocalDate.parse(scanner.next());
								} catch (final DateTimeParseException e) {
									System.out.println(e.getMessage());
									break;
								}
								System.out.println("Is it a VAT invoice? y/n");
								boolean isVat = false;
								switch (scanner.next()) {
									case YES:
										isVat = true;
										break;
									case NO:
										isVat = false;
										break;
								}
								var invoice = new Invoice.Builder(name, rate, hours)
										.date(date)
										.isVAT(isVat)
										.build();
								invoiceDao.addInvoice(invoice);
								scanner.nextLine();
								break;
							}
							case NO: {
								var invoice = new Invoice.Builder(name, rate, hours).build();
								invoiceDao.addInvoice(invoice);
								scanner.nextLine();
								break;
							}
							default: {
								System.out.println("Unrecognized command");
								scanner.nextLine();
								break;
							}
						}
						break;
					}
					case INVOICE_DELETE: {
						System.out.println("Please provide the name of invoice");
						var name = scanner.nextLine();

						invoiceDao.deleteInvoice(name);
						scanner.nextLine();
						break;
					}
					case INVOICE_ALL: {
						try {
							final var invoices = invoiceDao.getInvoices();
							invoices.forEach(invoice -> System.out.println(invoice.toString()));
						} catch (final SQLException e) {
							System.out.println(e.getMessage());
							scanner.nextLine();
						}
						break;
					}
					case INVOICE_ALL_MONTH: {
						try {
							var date = getDateForMonthQuery(scanner);
							final var invoices = invoiceDao.getInvoices(date);
							invoices.forEach(invoice -> System.out.println(invoice.toString()));
						} catch (final SQLException e) {
							System.out.println(e.getMessage());
							scanner.nextLine();
						}
					}
					case EXPENSE_ADD: {
						var expense = new Expense();
						System.out.println("Please provide the name of expense");
						expense.setName(scanner.nextLine());
						System.out.println("Please provide the value of expense");
						expense.setValue(scanner.nextDouble());

						expenseDao.addExpense(expense);
						scanner.nextLine();
						break;
					}
					case EXPENSE_UPDATE: {
						System.out.println("Please provide the name of expense");
						try {
							var expense = expenseDao.getExpense(scanner.nextLine());
							System.out.println("Please provide the new value of expense");
							expense.setValue(scanner.nextDouble());
							expenseDao.updateExpense(expense);
							scanner.nextLine();
						} catch (final SQLException e) {
							System.out.println(e.getMessage());
						}
						break;
					}
					case EXPENSE_DELETE: {
						System.out.println("Please provice the name of expense to delete");
						expenseDao.deleteExpense(scanner.nextLine());
						scanner.nextLine();
						break;
					}
					case EXPENSE_ALL: {
						try {
							final var expenses = expenseDao.getExpenses();
							expenses.forEach(expense -> System.out.println(expense.toString()));
						} catch (final SQLException e) {
							System.out.println(e.getMessage());
						}
						break;
					}
					default: {
						System.out.println("Unrecognized command");
					}
				}
			}
		} catch(final IllegalStateException |  NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
	}

	private static LocalDate getDateForMonthQuery(final Scanner scanner) {
		System.out.println("Please provide the year in format yyyy");
		var year = scanner.nextLine();
		System.out.println("Please provide the month in format MM");
		var month = scanner.nextLine();
		if (month.startsWith("0")) {
			month = month.substring(1);
		}
		return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
	}
}
