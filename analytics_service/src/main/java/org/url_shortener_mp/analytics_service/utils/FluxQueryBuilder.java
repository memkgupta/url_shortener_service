package org.url_shortener_mp.analytics_service.utils;

import java.util.*;

public class FluxQueryBuilder {
    private String bucket;
    private String rangeStart;
    private String rangeStop;
    private final List<String> filters = new ArrayList<>();
    private String aggregateEvery;
    private String aggregateFn;
    private String yield;

    public FluxQueryBuilder fromBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public FluxQueryBuilder range(String start, String stop) {
        this.rangeStart = start;
        this.rangeStop = stop;
        return this;
    }

    public FluxQueryBuilder filter(String field, String value) {
        filters.add("r." + field + " == \"" + value + "\"");
        return this;
    }

    public FluxQueryBuilder aggregateWindow(String every, String fn) {
        this.aggregateEvery = every;
        this.aggregateFn = fn;
        return this;
    }

    public FluxQueryBuilder yield(String name) {
        this.yield = name;
        return this;
    }


    public String build() {
        StringBuilder query = new StringBuilder();

        query.append("from(bucket: \"").append(bucket).append("\")\n");
        query.append("  |> range(start: ").append(rangeStart);
        if (rangeStop != null) {
            query.append(", stop: ").append(rangeStop);
        }
        query.append(")\n");

        if (!filters.isEmpty()) {
            query.append("  |> filter(fn: (r) => ");
            query.append(String.join(" and ", filters));
            query.append(")\n");
        }

        if (aggregateEvery != null && aggregateFn != null) {
            query.append("  |> aggregateWindow(every: ").append(aggregateEvery);
            query.append(", fn: ").append(aggregateFn).append(")\n");
        }

        if (yield != null) {
            query.append("  |> yield(name: \"").append(yield).append("\")");
        }

        return query.toString();
    }
}
