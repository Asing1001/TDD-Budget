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

        return queryBudgetInPeriod(new Period(start, end));
    }

    private Double queryBudgetInPeriod(Period period) {
        Double result = 0d;
        HashMap<YearMonth, Budget> budgetMap = convertAll();
        while (period.getStart().isBefore(period.getEnd()) || period.getStart().isEqual(period.getEnd())) {

            Budget budget = getCurrentBudget(budgetMap, period.getStart());

            LocalDate overlappingStart = period.getOverlappingStart(budget);
            LocalDate overlappingEnd = period.getOverlappingEnd(budget);
            long days = DAYS.between(overlappingStart, overlappingEnd) + 1;

            result += budget.getDailyAmount() * days;

            period.setStart(period.getStart().withDayOfMonth(1).plusMonths(1));
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
