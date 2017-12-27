package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class Metrics {
    @Value("${metrics.enable:false}")
    private boolean metricsEnabled;

    @Autowired
    private MetricRegistry metricRegistry;

    @Autowired
    private HtmlAggregateReporter reporter;

    public void updateMetric(String registerName, Long responseTime) {
        if (metricsEnabled) {
            Histogram histogram = metricRegistry.histogram(registerName);
            histogram.update(responseTime);
        }
    }

    @PreDestroy
    public void stop() {
        if (metricsEnabled) {
            reporter.report();
        }
    }
}