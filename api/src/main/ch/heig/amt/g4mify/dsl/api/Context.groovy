package ch.heig.amt.g4mify.dsl.api

import ch.heig.amt.g4mify.util.CounterAggregate
import ch.heig.amt.g4mify.util.CounterSpecResolver

/**
 * @author ldavid
 * @created 1/9/17
 */
class Context {

    private HashMap<String, CounterAggregate> counters = new HashMap<>()

    Context(List<CounterAggregate> counters) {
        for (counter in counters)
            this.counters[counter.name] = counter
    }

    long get(String name) {
        def spec = CounterSpecResolver.parse(name)
        return counters.get(spec.getCounter()).getMetric(spec.getMetric())
    }

}
