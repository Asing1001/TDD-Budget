package budget;

import java.time.LocalDate;
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
        HashMap<LocalDate, Budget> budgetMap = convertAll();
        LocalDate current = start;
        while (current.isBefore(end) || current.isEqual(end)) {

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

            Budget budget = getCurrentBudget(budgetMap, startOfCurrentMonth);
            result += budget.getDailyAmount() * days;

            current = startOfCurrentMonth.plusMonths(1);
        }

        return Math.round(result * 100.0) / 100.0;
    }

    private Budget getCurrentBudget(HashMap<LocalDate, Budget> budgetMap, LocalDate startOfCurrentMonth) {
        return budgetMap.getOrDefault(startOfCurrentMonth, new Budget(startOfCurrentMonth.format(ofPattern("yyyyMM")), 0));
    }

    private HashMap<LocalDate, Budget> convertAll() {
        HashMap<LocalDate, Budget> result = new HashMap<>();
        for (Budget budget : repo.getAll()) {
            result.put(budget.convertYearMonth(), budget);
        }
        return result;
    }

}
