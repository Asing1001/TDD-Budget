package budget;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
        Map<LocalDate, Integer> budgetMap = convertAll();
        LocalDate refStartDate = LocalDate.of(start.getYear(), start.getMonth(), start.getDayOfMonth());
        while(refStartDate.isBefore(end) || refStartDate.isEqual(end)) {

            // get the budget of this month
            LocalDate startOfMonth = LocalDate.of(refStartDate.getYear(), refStartDate.getMonth(), 1);
            int amount = budgetMap.containsKey(startOfMonth) ? budgetMap.get(startOfMonth) : 0;

            LocalDate refEndDate;
            LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
            if (end.isAfter(endOfMonth)){
                refEndDate = endOfMonth;
            } else {
                refEndDate = end;
            }

            // calculate month
            int days = refEndDate.getDayOfMonth() - refStartDate.getDayOfMonth() + 1;

            double calculatedAmount = days == refStartDate.lengthOfMonth()
                                      ? amount
                    : 1.0 * amount / refStartDate.lengthOfMonth() * days;

            result += calculatedAmount;

            refStartDate = startOfMonth.plusMonths(1);
        }

        result = Math.round(result * 100.0) / 100.0;

        return result;
    }

    private Map<LocalDate, Integer> convertAll() {
        HashMap<LocalDate, Integer> result = new HashMap<>();
        for (Budget budget : repo.getAll()) {
            result.put(budget.convertYearMonth(), budget.amount);
        }
        return result;

    }

}
