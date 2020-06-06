package com.gridnine.testing.filter.impl;

import com.gridnine.testing.filter.FilterQuery;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterQueryImpl implements FilterQuery {

    private final List<Flight> filteredFlights;

    public FilterQueryImpl(List<Flight> filteredFlights) {
        this.filteredFlights = filteredFlights;
    }

    @Override
    public List<Flight> get() {
        return this.filteredFlights;
    }

    @Override
    public FilterQuery filterFlights(Predicate<? super Flight> flightPredicate) {
        return new FilterQueryImpl(filteredFlights.stream()
                .filter(flightPredicate).collect(Collectors.toList()));
    }

    @Override
    public FilterQuery filterSegments(Predicate<? super Segment> segmentPredicate) {
        return new FilterQueryImpl(filteredFlights.stream()
                .filter(f -> f.getSegments().stream().noneMatch(segmentPredicate))
//                            List<Segment> temp = f.getSegments()
//                                    .stream().match.filter(segmentPredicate)
//                                    .collect(Collectors.toList());
//                            return !temp.isEmpty();
//                        })
                .collect(Collectors.toList()));
    }
}
