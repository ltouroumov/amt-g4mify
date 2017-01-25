package ch.heig.amt.g4mify.util;

import ch.heig.amt.g4mify.api.ApiException;
import ch.heig.amt.g4mify.model.Counter;
import ch.heig.amt.g4mify.model.Domain;
import ch.heig.amt.g4mify.model.Metric;
import ch.heig.amt.g4mify.repository.CountersRepository;
import ch.heig.amt.g4mify.repository.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ldavid
 * @created 1/9/17
 */
@Component
public class CounterSpecResolver {

    private final CountersRepository countersRepository;

    private final MetricsRepository metricsRepository;

    @Autowired
    public CounterSpecResolver(CountersRepository countersRepository, MetricsRepository metricsRepository) {
        this.countersRepository = countersRepository;
        this.metricsRepository = metricsRepository;
    }

    private static final Pattern counterPattern = Pattern.compile("^(\\w+)(.(\\w+))?$");

    public static CounterSpec parse(String counterSpec) {
        Matcher matcher = counterPattern.matcher(counterSpec);
        if (matcher.matches()) {
            String counterName = matcher.group(1);
            String metricName = Optional.ofNullable(matcher.group(3)).orElse("total");

            return new CounterSpec(counterName, metricName);
        } else {
            throw new ApiException("Counter reference is badly formatted");
        }
    }

    public Counter findCounter(Domain domain, String counterSpec) {
        CounterSpec spec = parse(counterSpec);
        return findCounter(domain, spec);
    }

    public Metric findMetric(Domain domain, String counterSpec) {
        CounterSpec spec = parse(counterSpec);
        return findMetric(domain, spec);
    }

    public Counter findCounter(Domain domain, CounterSpec spec) {
        return countersRepository.findByDomainAndName(domain, spec.getCounter())
                .orElseThrow(() -> new ApiException("Counter not found " + spec.getCounter()));
    }

    public Metric findMetric(Domain domain, CounterSpec spec) {
        return countersRepository.findByDomainAndName(domain, spec.getCounter())
                .map(counter -> metricsRepository.findByCounterAndName(counter, spec.getMetric())
                        .orElseThrow(() -> new ApiException("Metric not found " + spec.getMetric() + " for counter " + spec.getCounter())))
                .orElseThrow(() -> new ApiException("Counter not found " + spec.getCounter()));
    }

}
