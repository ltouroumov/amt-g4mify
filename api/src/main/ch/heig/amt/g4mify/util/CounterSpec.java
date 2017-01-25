package ch.heig.amt.g4mify.util;

/**
 * @author ldavid
 * @created 1/9/17
 */
public class CounterSpec {

    private String counter;

    private String metric;

    public CounterSpec(String counter, String metric) {
        this.counter = counter;
        this.metric = metric;
    }

    public String getCounter() {
        return counter;
    }

    public String getMetric() {
        return metric;
    }
}
