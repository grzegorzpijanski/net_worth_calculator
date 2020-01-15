package pijanski.grzegorz.networth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDao {

    private final Connection connection;

    public ExpenseDao(final Connection connection) {
        this.connection = connection;
    }

    public void addExpense(final Expense expense) {
        final var sql = "INSERT INTO expenses VALUES (?, ?)";
        try {
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, expense.getName());
            statement.setDouble(2, expense.getValue());
            statement.execute();
            System.out.println(String.format("%s added", expense.toString()));
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateExpense(final Expense expense) {
        final var sql = "UPDATE expenses SET value = ? WHERE name = ?";
        try {
            final var statement = connection.prepareStatement(sql);
            statement.setDouble(1, expense.getValue());
            statement.setString(2, expense.getName());
            statement.executeUpdate();
            System.out.println(String.format("%s updated", expense.toString()));
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteExpense(final String name) {
        final var sql = "DELETE FROM expenses WHERE name = ?";
        try {
            final var statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.execute();
            System.out.println(String.format("Expense %s was deleted", name));
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Expense> getExpenses() throws SQLException {
        final var sql = "SELECT * FROM expenses";

        final var statement = connection.createStatement();
        final ResultSet result = statement.executeQuery(sql);
        var expenses = new ArrayList<Expense>();

        while (result.next()) {
            final var name = result.getString("name");
            final var value = result.getDouble("value");

            final var expense = new Expense(name, value);
            expenses.add(expense);
        }

        return expenses;
    }

    public Expense getExpense(final String name) throws SQLException {
        final var sql = "SELECT * FROM expenses WHERE name = ? ";

        final var statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        final ResultSet result = statement.executeQuery();
        final var expenseName = result.getString("name");
        final var expenseValue = result.getDouble("value");

        return new Expense(expenseName, expenseValue);
    }
}
