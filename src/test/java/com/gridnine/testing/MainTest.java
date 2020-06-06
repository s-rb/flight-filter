package com.gridnine.testing;

import com.gridnine.testing.filter.FilterQueryBuilder;
import com.gridnine.testing.filter.filters.DepartureTimeFilter;
import com.gridnine.testing.filter.filters.GroundTimeFilter;
import com.gridnine.testing.filter.impl.FilterQueryBuilderImpl;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.FlightBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

import static com.gridnine.testing.Main.DELIMITER;
import static com.gridnine.testing.Main.TWO_HOURS_SECONDS;
import static org.junit.Assert.assertEquals;

public class MainTest {

    List<Flight> flights;
    LocalDateTime threeDaysFromNow;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream expectedOut = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        flights = FlightBuilder.createFlights();
        threeDaysFromNow = LocalDateTime.now().plusDays(3);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testMain() {
        Main.main(null);
        System.setOut(new PrintStream(expectedOut));

        printFlights("Source flights:", flights);
        FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilderImpl();
        List<Flight> flightsFiltered = filterQueryBuilder
                .select(flights)
                .filterSegments(
                        new DepartureTimeFilter().isBefore(LocalDateTime.now())).get();
        printFlights("Flights WITHOUT departed before now:", flightsFiltered);
        flightsFiltered = filterQueryBuilder
                .select(flights)
                .filterSegments(
                        new DepartureTimeFilter().isBefore(LocalDateTime.now()))
                .filterSegments(
                        s -> !s.getArrivalDate().isBefore(s.getDepartureDate()))
                .get();
        printFlights("Flights WITHOUT departed before now and WITHOUT arrive before depart:", flightsFiltered);
        flightsFiltered = filterQueryBuilder
                .select(flights)
                .filterSegments(
                        new DepartureTimeFilter().isBefore(LocalDateTime.now()))
                .filterSegments(
                        s -> !s.getArrivalDate().isBefore(s.getDepartureDate()))
                .filterFlights(
                        new GroundTimeFilter().isGreaterThan(TWO_HOURS_SECONDS))
                .get();
        printFlights("Flights WITHOUT departed before now " +
                "and WITHOUT arrive before depart and WITHOUT >2 Hours ground time:", flightsFiltered);

        assertEquals(expectedOut.toString(), outContent.toString());
    }

    private void printFlights(String msg, List<Flight> flights) {
        System.out.println(msg);
        flights.stream()
                .map(Flight::toString).forEach(s -> System.out.println("-\t" + s));
        System.out.println(DELIMITER + "\n");
    }

    @After
    public void restoreParams() {
        System.setOut(originalOut);
    }
}
