package pijanski.grzegorz.networth;

import java.time.LocalDate;

public class Invoice {
    private final String name;
    private final LocalDate date;
    private final double rate;
    private final double hours;
    private final boolean isVAT;

    public static class Builder {
        private final String name;
        private final double rate;
        private final double hours;

        private LocalDate date = LocalDate.now();
        private boolean isVAT = true;

        public Builder(final String name, final double rate, final double hours) {
            this.name = name;
            this.rate = rate;
            this.hours = hours;
        }

        public Builder date(final LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder isVAT(final boolean isVAT) {
            this.isVAT = isVAT;
            return this;
        }

        public Invoice build() {
            return new Invoice(this);
        }
    }
    private Invoice(final Builder builder) {
        name = builder.name;
        date = builder.date;
        rate = builder.rate;
        hours = builder.hours;
        isVAT = builder.isVAT;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getRate() {
        return rate;
    }

    public double getHours() {
        return hours;
    }

    public boolean isVAT() {
        return isVAT;
    }

    public double getTotal() {
        if (isVAT) {
            return rate * hours * 1.23;
        } else {
            return rate * hours;
        }
    }

    @Override
    public String toString() {
        return String.format("Invoice name: %s from %s with total %.2f", name, date.toString(), getTotal());
    }
}
