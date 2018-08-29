package budget;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;

import static java.time.format.DateTimeFormatter.ofPattern;

public class BudgetService {
    private IRepo repo;

    public BudgetService(IRepo repo) {
        this.repo = repo;
    }

    public Double queryBudget(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0d;
        }

        return queryBudgetInPeriod(new Period(start, end));
    }

    private Double queryBudgetInPeriod(Period period) {
        Double result = 0d;

        for (Budget budget : repo.getAll()) {
            result += budget.getDailyAmount() * period.getOverlappingDays(budget.getPeriod());
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
