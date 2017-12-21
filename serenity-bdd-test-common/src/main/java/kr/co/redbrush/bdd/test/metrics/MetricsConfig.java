package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Locale;

@Configuration
public class MetricsConfig {
    @Value("${metrics.warning.limit.for.responseTime:600}")
    private Long responseTimeWarningLimit;

    @Value("${metrics.template.path:metrics/metricsReportTemplate.htm")
    private String templateFile;

    @Value("${metrics.report.dir:target/site")
    private String reportDir;

    @Value("${metrics.report.file:target/site")
    private String reportFile;

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public HtmlAggregateReporter htmlReporter() {
        HtmlAggregateReporter reporter = HtmlAggregateReporter.builder()
                .reportDir(reportDir)
                .templateFile(templateFile)
                .locale(Locale.US)
                .startDate(new Date())
                .warningLimitNumber(responseTimeWarningLimit)
                .build();

        return reporter;
    }
}
