package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HtmlAggregateReporterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String templateFilePath = "/template/metricsReportTemplate.htm";
    private HtmlAggregateReporter reporter;

    @Mock
    private MetricRegistry registry;

    private long responseTimeWarningLimit = 1000L;
    private File templateFile;

    @Before
    public void before() {
        templateFile = new File(this.getClass().getResource(templateFilePath).getFile());
        temporaryFolder.getRoot();

        reporter = HtmlAggregateReporter.forRegistry(registry)
                .formatFor(Locale.US)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.SECONDS)
                .warningLimitNumber(responseTimeWarningLimit)
                .templateHtmlFile(templateFile)
                .build(temporaryFolder.getRoot());
    }

    @Test
    public void testReport() {
        // https://stackoverflow.com/questions/44405897/org-apache-velocity-exception-resourcenotfoundexception-unable-to-find-resource
        /*
VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init();
        Template tempalte = ve.getTemplate("templates/email/test.vm");
         */
        reporter.report();
    }
}
