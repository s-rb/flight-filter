package com.gridnine.testing.filter.impl;

import com.gridnine.testing.filter.FilterQuery;
import com.gridnine.testing.filter.FilterQueryBuilder;
import com.gridnine.testing.model.Flight;

import java.util.List;

public class FilterQueryBuilderImpl implements FilterQueryBuilder {

    @Override
    public FilterQuery select(List<Flight> flights) {
        return new FilterQueryImpl(flights);
    }
}
