package budget;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.DAYS;

public class BudgetService {
    private IRepo repo;

    public BudgetService(IRepo repo) {
        this.repo = repo;
    }

    public Double queryBudget(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0d;
        }

        Double result = 0d;
        HashMap<YearMonth, Budget> budgetMap = convertAll();
        LocalDate current = start;
        while (current.isBefore(end) || current.isEqual(end)) {

            Budget budget = getCurrentBudget(budgetMap, current);

            LocalDate refStartDate;
            LocalDate startOfCurrentMonth = current.withDayOfMonth(1);
            if (start.isBefore(startOfCurrentMonth)) {
                refStartDate = startOfCurrentMonth;
            } else {
                refStartDate = start;
            }

            LocalDate refEndDate;
            LocalDate endOfCurrentMonth = current.withDayOfMonth(current.lengthOfMonth());
            if (end.isAfter(endOfCurrentMonth)) {
                refEndDate = endOfCurrentMonth;
            } else {
                refEndDate = end;
            }

            long days = DAYS.between(refStartDate, refEndDate) + 1;

            result += budget.getDailyAmount() * days;

            current = startOfCurrentMonth.plusMonths(1);
        }

        return Math.round(result * 100.0) / 100.0;
    }

    private Budget getCurrentBudget(HashMap<YearMonth, Budget> budgetMap, LocalDate current) {
        return budgetMap.getOrDefault(YearMonth.from(current), new Budget(current.format(ofPattern("yyyyMM")), 0));
    }

    private HashMap<YearMonth, Budget> convertAll() {
        HashMap<YearMonth, Budget> result = new HashMap<>();
        for (Budget budget : repo.getAll()) {
            result.put(budget.convertYearMonth(), budget);
        }
        return result;
    }

}
