package com.gridnine.testing.model;

import junit.framework.TestCase;
import org.hamcrest.collection.IsEmptyCollection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class FlightBuilderTest extends TestCase {

    public void testCreateFlights() {
        Comparator<Flight> flightComparator = Comparator.comparing(o -> o.getSegments().get(0).getDepartureDate());
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);

        List<Flight> actual = FlightBuilder.createFlights();
        List<Flight> expected = Arrays.asList(
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),
                createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                        threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
        actual.sort(flightComparator);
        expected.sort(flightComparator);

        assertNotNull(actual);
        assertThat(actual, not(IsEmptyCollection.empty()));
        assertThat(actual.size(), is(expected.size()));
        assertEquals(Arrays.toString(expected.toArray()), Arrays.toString(actual.toArray()));
    }

    private Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                    "you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
}
