package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.SortedMap;
import java.util.Timer;

@Slf4j
@Builder
public class HtmlAggregateReporter extends ScheduledReporter {
    private static final String VELOCITY_PROPERTIES_FILE = "velocity.properties";
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static String htmlReportEnd = "</body>\n</html>";
    private static String tableEnd = "</tbody></table>\n";

    private String reportDir;
    private String reportFileName;
    private String templateFile;
    private Locale locale;
    private Long warningLimitNumber;
    @Builder.Default
    private Date startDate = new Date();

    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        if (StringUtils.isEmpty(templateFile)) {
            LOGGER.warn("Can not find template report file! Report will not be generated.");
        } else {
            Velocity.init(VELOCITY_PROPERTIES_FILE);

            VelocityContext context = new VelocityContext();
            context.put("histograms", histograms);

            FileWriter writer = null;

            try {
                Template template = Velocity.getTemplate(templateFile);

                File reportFile = new File(reportDir, reportFileName);
                writer = new FileWriter(reportFile);

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
}
