package budget;

import java.time.LocalDate;

public class Period {
    private final LocalDate end;
    private LocalDate start;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalDate getOverlappingStart(Budget budget) {
        return start.isBefore(budget.getFirstDay()) ? budget.getFirstDay() : start;
    }

    public LocalDate getOverlappingEnd(Budget budget) {
        return end.isAfter(budget.getLastDay()) ? budget.getLastDay() : end;
    }
}
