package pijanski.grzegorz.networth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDao {

    private final Connection connection;

    public InvoiceDao(final Connection connection) {
        this.connection = connection;
    }

    public void addInvoice(final Invoice invoice) {
        final var sql = "INSERT INTO invoices (name, date, rate, hours, isVAT) VALUES (?, ?, ?, ?, ?)";

        try {
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, invoice.getName());
            statement.setString(2, invoice.getDate().toString());
            statement.setDouble(3, invoice.getRate());
            statement.setDouble(4, invoice.getHours());
            statement.setBoolean(5, invoice.isVAT());

            statement.execute();
            System.out.println(String.format("%s added", invoice.toString()));
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteInvoice(final String name) {
        final var sql = "DELETE FROM invoices WHERE name = ?";

        try {
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, name);

            statement.execute();
            System.out.println(String.format("Expense %s deleted", name));
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Invoice> getInvoices() throws SQLException {
        final String sql = "SELECT * FROM invoices";
        final var statement = connection.createStatement();
        final ResultSet result = statement.executeQuery(sql);

        return getInvoices(result);
    }

    public List<Invoice> getInvoices(final LocalDate dateQuery) throws SQLException {
        final String sql = "SELECT * FROM invoices WHERE strftime('%Y', date) = ? AND strftime('%m', date) = ?";

        final var statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(dateQuery.getYear()));

        int monthQuery = dateQuery.getMonthValue();
        if (monthQuery > 9) {
            statement.setString(2, String.valueOf(monthQuery));
        } else {
            statement.setString(2, String.format("%02d", monthQuery));
        }

        final ResultSet result = statement.executeQuery();

        return getInvoices(result);
    }

    private List<Invoice> getInvoices(final ResultSet result) throws SQLException {
        var invoices = new ArrayList<Invoice>();

        while (result.next()) {
            final var name = result.getString("name");
            final LocalDate date = LocalDate.parse(result.getString("date"));
            final double rate = result.getDouble("rate");
            final double hours = result.getDouble("hours");
            final boolean isVAT = result.getBoolean("isVAT");

            final var invoice = new Invoice.Builder(name, rate, hours)
                    .date(date)
                    .isVAT(isVAT)
                    .build();

            invoices.add(invoice);
        }
        return invoices;
    }
}
