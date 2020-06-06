package com.gridnine.testing.filter.filters;

import com.gridnine.testing.model.Segment;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

public class DepartureTimeFilter implements TimeFilter {


    @Override
    public Predicate<Segment> isBefore(LocalDateTime time) {
        return s -> s.getDepartureDate().isBefore(Objects.requireNonNull(time));
    }

    @Override
    public Predicate<Segment> isAfter(LocalDateTime time) {
        return s -> s.getDepartureDate().isAfter(Objects.requireNonNull(time));
    }

    // С точностью до минут (секунды при сравнении обнуляются)
    @Override
    public Predicate<Segment> isEqual(LocalDateTime time) {
        return s -> s.getDepartureDate().withSecond(0).withNano(0)
                .isEqual(Objects.requireNonNull(time).withSecond(0).withNano(0));
    }
}
