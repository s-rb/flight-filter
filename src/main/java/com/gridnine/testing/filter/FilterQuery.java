package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.util.List;
import java.util.function.Predicate;

public interface FilterQuery {

    List<Flight> get();

    FilterQuery filterFlights(Predicate<? super Flight> flightPredicate);

    FilterQuery filterSegments(Predicate<? super Segment> segmentPredicate);
}
