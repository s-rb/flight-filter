package com.gridnine.testing.filter.filters;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class GroundTimeFilter {

    public Predicate<Flight> isGreaterThan(Long groundTimeLimitSec) {
        return f -> !compareGroundTimeToLimit(f, Objects.requireNonNull(groundTimeLimitSec));
    }

    public Predicate<Flight> isLessThan(Long groundTimeLimitSec) {
        return f -> compareGroundTimeToLimit(f, Objects.requireNonNull(groundTimeLimitSec));
    }

    private boolean compareGroundTimeToLimit(Flight f, long groundTimeLimit) {
        boolean isGreaterThanLimit = false;
        List<Segment> segments = f.getSegments();
        long sumLandTimeSec = 0;

        for (int i = 0; i < segments.size() - 1; i++) {
            long departureTimeNextSegment = segments.get(i + 1).getDepartureDate()
                    .toInstant(ZoneOffset.UTC).getEpochSecond();
            long arriveTimeThisSegment = segments.get(i).getArrivalDate()
                    .toInstant(ZoneOffset.UTC).getEpochSecond();
            long landTimeSec = departureTimeNextSegment - arriveTimeThisSegment;

            sumLandTimeSec += landTimeSec;

            if (sumLandTimeSec > groundTimeLimit) {
                isGreaterThanLimit = true;
                break;
            }
        }
        return isGreaterThanLimit;
    }


}
