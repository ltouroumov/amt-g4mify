package ch.heig.amt.g4mify.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ldavid
 * @created 1/9/17
 */
public class CounterAggregate {

    private String name;

    private Map<String, Long> metrics;

    public CounterAggregate(String name) {
        this.name = name;
        this.metrics = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<String, Long> getMetrics() {
        return metrics;
    }

    public void setMetric(String name, long value) {
        metrics.put(name, value);
    }

    public long getMetric(String name) {
        return metrics.get(name);
    }

}
