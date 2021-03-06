package com.gridnine.testing.filter.impl;

import com.gridnine.testing.filter.FilterQueryBuilder;
import com.gridnine.testing.filter.filters.ArriveTimeFilter;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.FlightBuilder;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.gridnine.testing.TestUtils.createFlight;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArriveTimeFilterTests {

    List<Flight> flights;
    Comparator<Flight> flightComparator = Comparator.comparing(o -> o.getSegments().get(0).getDepartureDate());
    LocalDateTime threeDaysFromNow;
    FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilderImpl();

    @Before
    public void setUp() {
        flights = FlightBuilder.createFlights();
        threeDaysFromNow = LocalDateTime.now().plusDays(3);
    }

    @Test
    public void testFilterSegments_ArriveTimeIsBefore() {
        List<Flight> actual = filterQueryBuilder.select(flights)
                .filterSegments(new ArriveTimeFilter().isBefore(threeDaysFromNow.plusHours(1))).get();

        List<Flight> expected = Arrays.asList(
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                        threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
        actual.sort(flightComparator);
        expected.sort(flightComparator);
        getAsserts(actual, expected);
    }

    @Test
    public void testFilterSegments_ArriveTimeIsAfter() {
        List<Flight> actual = filterQueryBuilder.select(flights)
                .filterSegments(new ArriveTimeFilter().isAfter(threeDaysFromNow.plusHours(4))).get();

        List<Flight> expected = Arrays.asList(
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)));
        actual.sort(flightComparator);
        expected.sort(flightComparator);
        getAsserts(actual, expected);
    }

    @Test
    public void testFilterSegments_ArriveTimeEquals() {
        List<Flight> actual = filterQueryBuilder.select(flights)
                .filterSegments(new ArriveTimeFilter().isEqual(threeDaysFromNow.plusHours(2))).get();

        List<Flight> expected = Arrays.asList(
                createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6))
        );
        actual.sort(flightComparator);
        expected.sort(flightComparator);
        getAsserts(actual, expected);
    }

    private void getAsserts(List<Flight> actual, List<Flight> expected) {
        assertNotNull(actual);
        assertThat(actual, not(IsEmptyCollection.empty()));
        assertThat(actual.size(), is(expected.size()));
        assertEquals(Arrays.toString(expected.toArray()), Arrays.toString(actual.toArray()));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterSegments_ArriveTimeIsBefore_NullParam() {
        List<Flight> actual = filterQueryBuilder.select(flights)
                .filterSegments(new ArriveTimeFilter().isBefore(null)).get();
    }

    @Test(expected = NullPointerException.class)
    public void testFilterSegments_ArriveTimeIsAfter_NullParam() {
        List<Flight> actual = filterQueryBuilder.select(flights)
                .filterSegments(new ArriveTimeFilter().isAfter(null)).get();
    }

    @Test(expected = NullPointerException.class)
    public void testFilterSegments_ArriveTimeIsEqual_NullParam() {
        List<Flight> actual = filterQueryBuilder.select(flights)
                .filterSegments(new ArriveTimeFilter().isEqual(null)).get();
    }
}
