package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
public class HtmlAggregateReporterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private MetricRegistry registry = new MetricRegistry();
    private String templateFilePath = "/metrics/template/test_metricsReportTemplate.htm";
    private HtmlAggregateReporter reporter;

    private long responseTimeWarningLimit = 1000L;
    private File templateFile;

    @Before
    public void before() {
        templateFile = new File(templateFilePath);
        temporaryFolder.getRoot();

        LOGGER.debug("templateFile : {}, exists : {}", templateFile, templateFile.exists());

        reporter = HtmlAggregateReporter.forRegistry(registry)
                .formatFor(Locale.US)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.SECONDS)
                .warningLimitNumber(responseTimeWarningLimit)
                .templateHtmlFile(templateFile)
                .build(temporaryFolder.getRoot());
    }

    @Test
    public void testReport() throws Exception {
        String expectedResult = "Count : 1, Max : 100";

        Histogram histogram = registry.histogram("test item");
        histogram.update(100L);

        reporter.report();

        File reportFile = new File(temporaryFolder.getRoot().getPath() + "/" + HtmlAggregateReporter.REPORT_FILE_NAME);

        LOGGER.debug("Report\n{}", FileUtils.readFileToString(reportFile, "UTF-8"));

        assertThat("Unexpected report.", FileUtils.readFileToString(reportFile, "UTF-8"), is(expectedResult));
    }
}
