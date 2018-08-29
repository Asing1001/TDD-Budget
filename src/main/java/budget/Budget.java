package budget;

import java.time.YearMonth;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Budget {

    public String yearMonth;
    public int amount;

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public YearMonth convertYearMonth() {
        return YearMonth.parse(yearMonth, ofPattern("yyyyMM"));
    }

    public double getDailyAmount() {
        return 1.0 * amount / convertYearMonth().lengthOfMonth();
    }
}
