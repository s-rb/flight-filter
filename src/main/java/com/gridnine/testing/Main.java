package com.gridnine.testing;

import com.gridnine.testing.filter.FilterQueryBuilder;
import com.gridnine.testing.filter.filters.DepartureTimeFilter;
import com.gridnine.testing.filter.filters.GroundTimeFilter;
import com.gridnine.testing.filter.impl.FilterQueryBuilderImpl;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.FlightBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static final Long TWO_HOURS_SECONDS = 60L * 60L * 2L;
    public static final String DELIMITER = "--------------------";

    public static void main(String[] args) {
        // Source flights
        List<Flight> flights = FlightBuilder.createFlights();
        printFlights("Source flights:", flights);

        FilterQueryBuilder filterQueryBuilder = new FilterQueryBuilderImpl();

        // Filtered: res without departed before now
        List<Flight> flightsFiltered = filterQueryBuilder
                .select(flights)
                .filterSegments(
                        new DepartureTimeFilter().isBefore(LocalDateTime.now())).get();
        printFlights("Flights WITHOUT departed before now:", flightsFiltered);


        // Filtered: res without departed before now + Flights arrive before depart
        flightsFiltered = filterQueryBuilder
                .select(flights)
                .filterSegments(
                        new DepartureTimeFilter().isBefore(LocalDateTime.now()))
                .filterSegments(
                        s -> !s.getArrivalDate().isBefore(s.getDepartureDate()))
                .get();
        printFlights("Flights WITHOUT departed before now and WITHOUT arrive before depart:", flightsFiltered);


        // Filtered: res without departed before now + Flights arrive before depart + Flights with >2 Hours ground time
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
    }

    private static void printFlights(String msg, List<Flight> flights) {
        System.out.println(msg);
        flights.stream()
                .map(Flight::toString).forEach(s -> System.out.println("-\t" + s));
        System.out.println(DELIMITER + "\n");
    }
}