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

    public long getOverlappingDays(Period another) {
        LocalDate overlappingStart = start.isBefore(another.start) ? another.start : start;
        LocalDate overlappingEnd = end.isAfter(another.end) ? another.end : end;

        if (overlappingStart.isAfter(overlappingEnd)) {
            return 0;
        }

        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
