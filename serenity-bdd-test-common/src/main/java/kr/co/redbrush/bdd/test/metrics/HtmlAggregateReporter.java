package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;
import java.util.Date;
import java.util.Locale;
import java.util.SortedMap;

@Slf4j
@Builder
public class HtmlAggregateReporter extends ScheduledReporter {
    private static final String VELOCITY_PROPERTIES_FILE = "velocity.properties";

    private String reportDir;
    private String reportFileName;
    private String templateFile;
    private Locale locale;
    private Long warningLimitNumber;
    @Builder.Default
    private Date startDate = new Date();

    @Override
    public void report(SortedMap<String, Gauge> guages, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        Velocity.init(VELOCITY_PROPERTIES_FILE);

        VelocityContext context = new VelocityContext();
        context.put("histograms", histograms);

        generateReportFile(context);
    }

    private void generateReportFile(VelocityContext context) {
        Writer writer = null;

        try {
            Template template = Velocity.getTemplate(templateFile);

            File reportFile = new File(reportDir, reportFileName);
            writer = new OutputStreamWriter(new FileOutputStream(reportFile), "UTF-8");

            template.merge(context, writer);
        } catch (ResourceNotFoundException rnfe) {
            LOGGER.warn("Template file not found. File : {}\n{}", templateFile, rnfe);
        } catch (ParseErrorException pee) {
            LOGGER.warn("Template file parse failed. File : {}\n{}", templateFile, pee);
        } catch (IOException ioe) {
            LOGGER.warn("Can't write report File : {}\n{}", templateFile, ioe);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
