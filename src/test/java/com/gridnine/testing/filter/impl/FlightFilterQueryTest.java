package com.gridnine.testing.filter.impl;

import com.gridnine.testing.filter.FilterQuery;
import com.gridnine.testing.filter.FilterQueryBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.FlightBuilder;
import com.gridnine.testing.model.Segment;
import junit.framework.TestCase;
import org.hamcrest.collection.IsEmptyCollection;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class FlightFilterQueryTest extends TestCase {


    @Override
    public void setUp() throws Exception {
    }

    public void testCreateFilterQuery() {
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

        FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilderImpl();
        FilterQuery actualFilterQuery = filterQueryBuilder.select(actual);
        FilterQuery expectedFilterQuery = new FilterQueryImpl(expected);

        assertNotNull(actualFilterQuery);
        assertThat(actualFilterQuery.get(), not(IsEmptyCollection.empty()));
        assertThat(actualFilterQuery.get().size(), is(expectedFilterQuery.get().size()));

        actualFilterQuery.get().sort(flightComparator);
        expectedFilterQuery.get().sort(flightComparator);
        assertEquals(Arrays.toString(expectedFilterQuery.get().toArray()), Arrays.toString(actual.toArray()));
    }

    public void testCreateFilterQuery_NullFlights() {
        FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilderImpl();
        FilterQuery actualFilterQuery = filterQueryBuilder.select(null);
        assertNull(actualFilterQuery.get());
    }

    public void testCreateFilterQuery_EmptyFlightList() {
        List<Flight> flights = Collections.emptyList();
        FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilderImpl();
        FilterQuery actualFilterQuery = filterQueryBuilder.select(flights);
        assertNotNull(actualFilterQuery.get());
        assertThat(actualFilterQuery.get(), IsEmptyCollection.empty());
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
