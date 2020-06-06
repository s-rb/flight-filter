package com.gridnine.testing.filter.filters;

import com.gridnine.testing.model.Segment;

import java.time.LocalDateTime;
import java.util.function.Predicate;

public interface TimeFilter {

    Predicate<Segment> isBefore(LocalDateTime time);

    Predicate<Segment> isAfter(LocalDateTime time);

    Predicate<Segment> isEqual(LocalDateTime time);
}
