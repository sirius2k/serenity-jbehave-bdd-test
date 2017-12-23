package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HtmlAggregateReporter extends ScheduledReporter {
    public static final String REPORT_FILE_NAME = "metrics_report.html";
    private static final String VELOCITY_PROPERTIES_FILE = "velocity.properties";
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final File directory;
    private final Locale locale;
    private final Clock clock;
    private final Long warningLimitNumber;
    private final File templateHtmlFile;

    public static HtmlAggregateReporter.Builder forRegistry(MetricRegistry registry) {
        return new HtmlAggregateReporter.Builder(registry);
    }

    private HtmlAggregateReporter(MetricRegistry registry, File directory, Locale locale, TimeUnit rateUnit, TimeUnit durationUnit, Clock clock, MetricFilter filter, Long warningLimitNumber, File templateHtmlFile) {
        super(registry, "html-aggregate-reporter", filter, rateUnit, durationUnit);
        this.directory = directory;
        this.locale = locale;
        this.clock = clock;
        this.warningLimitNumber = warningLimitNumber;
        this.templateHtmlFile = templateHtmlFile;
    }

    @Override
    public void report(SortedMap<String, Gauge> guages, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, com.codahale.metrics.Timer> timers) {
        Velocity.init(VELOCITY_PROPERTIES_FILE);

        VelocityContext context = new VelocityContext();
        context.put("histograms", histograms);

        generateReportFile(context);
    }

    private void generateReportFile(VelocityContext context) {
        Writer writer = null;

        try {
            LOGGER.debug("templateHtmlFileName : {}", templateHtmlFile.getName());
            Template template = Velocity.getTemplate(templateHtmlFile.getName());

            File reportFile = new File(directory, REPORT_FILE_NAME);
            writer = new OutputStreamWriter(new FileOutputStream(reportFile), UTF_8);

            template.merge(context, writer);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.warn("Template file not found. File : {}\n{}", templateHtmlFile, rnfe);
        } catch (ParseErrorException pee) {
            LOGGER.warn("Template file parse failed. File : {}\n{}", templateHtmlFile, pee);
        } catch (IOException ioe) {
            LOGGER.warn("Can't write report File : {}\n{}", templateHtmlFile, ioe);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                LOGGER.warn("Writer has not been closed. : {}", e);
            }
        }
    }

    public static class Builder {
        private final MetricRegistry registry;
        private Locale locale;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private Clock clock;
        private MetricFilter filter;
        private Long warningLimitNumber;
        private File templateHtmlFile;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.locale = Locale.getDefault();
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.clock = Clock.defaultClock();
            this.filter = MetricFilter.ALL;
            this.warningLimitNumber = -1L;
        }

        public HtmlAggregateReporter.Builder formatFor(Locale locale) {
            this.locale = locale;
            return this;
        }

        public HtmlAggregateReporter.Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        public HtmlAggregateReporter.Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        public HtmlAggregateReporter.Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public HtmlAggregateReporter.Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        public HtmlAggregateReporter.Builder templateHtmlFile(File templateHtmlFile) {
            this.templateHtmlFile = templateHtmlFile;
            return this;
        }

        public HtmlAggregateReporter.Builder warningLimitNumber(Long number) {
            this.warningLimitNumber = number;
            return this;
        }

        public HtmlAggregateReporter build(File directory) {
            return new HtmlAggregateReporter(this.registry, directory, this.locale, this.rateUnit, this.durationUnit, this.clock, this.filter, this.warningLimitNumber, this.templateHtmlFile);
        }
    }
}
