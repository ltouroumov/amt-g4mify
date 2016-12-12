package ch.heig.amt.g4mify.model.view.counter;

import ch.heig.amt.g4mify.model.view.metric.MetricSummary;

import java.util.List;

/**
 * @author ldavid
 * @created 12/7/16
 */
public class CounterSummary {

    public long id;

    public String name;

    public List<MetricSummary> metrics;

}
