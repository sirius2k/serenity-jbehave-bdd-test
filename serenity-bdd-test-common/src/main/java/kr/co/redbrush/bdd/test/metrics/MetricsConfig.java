package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfig {
    @Value("${metrics.warning.limit.for.responseTime:600}")
    private Long responseTimeWarningLimit;

    @Value("${metrics.template.path:metrics/metricsReportTemplate.htm")
    private String templateFile;

    @Value("${metrics.report.dir:target/site")
    private String reportDir;

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public HtmlAggregateReporter htmlReporter() {
        HtmlAggregateReporter reporter = HtmlAggregateReporter.forRegistry(metricRegistry())
                .formatFor(Locale.US)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.SECONDS)
                .warningLimitNumber(responseTimeWarningLimit)
                .templateHtmlFile(new File(templateFile))
                .build(new File(reportDir));

        return reporter;
    }
}

