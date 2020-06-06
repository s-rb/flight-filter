package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;

import java.util.List;

public interface FilterQueryBuilder {

    FilterQuery select(List<Flight> flights);
}
