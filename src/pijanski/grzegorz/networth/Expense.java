package pijanski.grzegorz.networth;

public class Expense {

    private String name;

    private double value;

    public Expense() { }

    public Expense(final String name, final double value) {
        this.name = name;
        this.value = value;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setValue(final double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Expense with name: %s and value: %.2f", name, value);
    }
}
