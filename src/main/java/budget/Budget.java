package budget;

import java.time.LocalDate;
import java.time.YearMonth;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Budget {

    public String yearMonth;
    public int amount;

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public YearMonth yearMonth() {
        return YearMonth.parse(yearMonth, ofPattern("yyyyMM"));
    }

    public double getDailyAmount() {
        return 1.0 * amount / yearMonth().lengthOfMonth();
    }

    public LocalDate getFirstDay() {
        return yearMonth().atDay(1);
    }

    public LocalDate getLastDay() {
        return yearMonth().atEndOfMonth();
    }

    public Period getPeriod() {
        return new Period(getFirstDay(), getLastDay());
    }

    public double getOverlappingAmount(Period period) {
        return getDailyAmount() * period.getOverlappingDays(getPeriod());
    }
}
