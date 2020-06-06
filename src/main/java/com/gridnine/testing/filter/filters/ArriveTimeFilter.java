package com.gridnine.testing.filter.filters;

import com.gridnine.testing.model.Segment;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

public class ArriveTimeFilter implements TimeFilter {

    @Override
    public Predicate<Segment> isBefore(LocalDateTime time) {
        return s -> s.getArrivalDate().isBefore(Objects.requireNonNull(time));
    }

    @Override
    public Predicate<Segment> isAfter(LocalDateTime time) {
        return s -> s.getArrivalDate().isAfter(Objects.requireNonNull(time));
    }

    @Override
    public Predicate<Segment> isEqual(LocalDateTime time) {
        return s -> s.getArrivalDate().withSecond(0).withNano(0)
                .isEqual(Objects.requireNonNull(time).withSecond(0).withNano(0));
    }
}
