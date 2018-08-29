package budget;

import java.time.LocalDate;

public class BudgetService {
    private IRepo repo;

    public BudgetService(IRepo repo) {
        this.repo = repo;
    }

    public Double queryBudget(LocalDate start, LocalDate end) {
        Period period = new Period(start, end);

        Double result = 0d;

        for (Budget budget : repo.getAll()) {
            result += budget.getOverlappingAmount(period);
        }

        return Math.round(result * 100.0) / 100.0;
    }

}
