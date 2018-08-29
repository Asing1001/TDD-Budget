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
            if (start.isBefore(budget.getFirstDay())) {
                refStartDate = budget.getFirstDay();
            } else {
                refStartDate = start;
            }

            LocalDate refEndDate;
            if (end.isAfter(budget.getLastDay())) {
                refEndDate = budget.getLastDay();
            } else {
                refEndDate = end;
            }

            long days = DAYS.between(refStartDate, refEndDate) + 1;

            result += budget.getDailyAmount() * days;

            current = current.withDayOfMonth(1).plusMonths(1);
        }

        return Math.round(result * 100.0) / 100.0;
    }

    private Budget getCurrentBudget(HashMap<YearMonth, Budget> budgetMap, LocalDate current) {
        return budgetMap.getOrDefault(YearMonth.from(current), new Budget(current.format(ofPattern("yyyyMM")), 0));
    }

    private HashMap<YearMonth, Budget> convertAll() {
        HashMap<YearMonth, Budget> result = new HashMap<>();
        for (Budget budget : repo.getAll()) {
            result.put(budget.yearMonth(), budget);
        }
        return result;
    }

}
