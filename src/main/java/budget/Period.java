package budget;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

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

    public long getOverlappingDays(Period another) {
        LocalDate overlappingStart = start.isBefore(another.start) ? another.start : start;
        LocalDate overlappingEnd = end.isAfter(another.end) ? another.end : end;
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
