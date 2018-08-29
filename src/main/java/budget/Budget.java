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

    public double getOverlappingAmount(Period period) {
        return getDailyAmount() * period.getOverlappingDays(getPeriod());
    }

    private YearMonth yearMonth() {
        return YearMonth.parse(yearMonth, ofPattern("yyyyMM"));
    }

    private double getDailyAmount() {
        return 1.0 * amount / yearMonth().lengthOfMonth();
    }

    private LocalDate getFirstDay() {
        return yearMonth().atDay(1);
    }

    private LocalDate getLastDay() {
        return yearMonth().atEndOfMonth();
    }

    private Period getPeriod() {
        return new Period(getFirstDay(), getLastDay());
    }
}
