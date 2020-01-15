package pijanski.grzegorz.networth;

import java.util.List;

public class NetWorthCalculator {

    private NetWorthCalculator() {
        throw new AssertionError();
    }

    public static double calculate(final List<Invoice> invoices, final List<Expense> expenses) {
        double sumOfExpenses = expenses.stream()
                .mapToDouble(Expense::getValue)
                .sum();

        double sumOfInvoices = invoices.stream()
                .mapToDouble(Invoice::getTotal)
                .sum();

        return sumOfInvoices - sumOfExpenses;
    }
}
