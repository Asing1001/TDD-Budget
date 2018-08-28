package budget;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static java.time.LocalDate.of;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BudgetServiceTest {

    IRepo repo = mock(IRepo.class);
    BudgetService budgetService = new BudgetService(repo);

    @Test
    public void budget_one_month_0801_0831() {
        givenBudgetList("201808", 310);

        totalAmountShouldBe(310.0,
                of(2018, 8, 1),
                of(2018, 8, 31));
    }

    @Test
    public void budget_one_month_0810_0820() {
        givenBudgetList("201808", 310);

        totalAmountShouldBe(110.0,
                of(2018, 8, 10),
                of(2018, 8, 20));
    }

    @Test
    public void budget_one_month_0810_0901_overlap_budget_last_day() {
        givenBudgetList("201808", 310);

        totalAmountShouldBe(220,
                of(2018, 8, 10),
                of(2018, 9, 1));
    }

    @Test
    public void overlap_budget_first_day() {
        givenBudgetList("201808", 310);

        totalAmountShouldBe(10,
                of(2018, 7, 31),
                of(2018, 8, 1));
    }

    @Test
    public void without_overlap_before_budget() {
        givenBudgetList("201808", 310);

        totalAmountShouldBe(0,
                of(2018, 7, 29),
                of(2018, 7, 30));
    }

    @Test
    public void without_overlap_after_budget() {
        givenBudgetList("201808", 310);

        totalAmountShouldBe(0,
                of(2018, 9, 10),
                of(2018, 9, 20));
    }

    @Test
    public void budget_two_month_0831_0901() {
        givenBudgetList(
                new Budget("201808", 310),
                new Budget("201809", 300));

        totalAmountShouldBe(20,
                of(2018, 8, 31),
                of(2018, 9, 1));
    }

    @Test
    public void two_month() {
        givenBudgetList(
                new Budget("201808", 310),
                new Budget("201809", 300));

        totalAmountShouldBe(320,
                of(2018, 8, 1),
                of(2018, 9, 1));
    }

    @Test
    public void daily_amount_is_point_5() {
        givenBudgetList("201806", 15);

        totalAmountShouldBe(0.5,
                of(2018, 6, 1),
                of(2018, 6, 1));
    }

    private void givenBudgetList(Budget... budgets) {
        when(repo.getAll()).thenReturn(Arrays.asList(
                budgets
        ));
    }

    private void givenBudgetList(String yearMonth, int amount) {
        when(repo.getAll()).thenReturn(Arrays.asList(
                new Budget(yearMonth, amount)
        ));
    }

    private void totalAmountShouldBe(double expected, LocalDate startDate, LocalDate endDate) {
        Assert.assertEquals(expected, budgetService.queryBudget(startDate, endDate), 0.001);
    }
}