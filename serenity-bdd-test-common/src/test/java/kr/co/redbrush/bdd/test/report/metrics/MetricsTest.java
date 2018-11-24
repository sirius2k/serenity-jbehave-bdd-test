package kr.co.redbrush.bdd.test.report.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class MetricsTest {
    @InjectMocks
    private Metrics metrics;

    @Mock
    private MetricRegistry metricRegistry;

    @Mock
    private HtmlAggregateReporter reporter;

    @Mock
    private Histogram histogram;

    private String registerName = "testName";
    private Long responseTime = 100L;

    @Before
    public void before() {
        when(metricRegistry.histogram(registerName)).thenReturn(histogram);
    }

    @Test
    public void testUpdateMetric() {
        ReflectionTestUtils.setField(metrics, "metricsEnabled", true);

        metrics.updateMetric(registerName, responseTime);

        verify(histogram).update(responseTime);
    }

    @Test
    public void testUpdateMetricWhenDisabled() {
        ReflectionTestUtils.setField(metrics, "metricsEnabled", false);

        metrics.updateMetric(registerName, responseTime);

        verify(histogram, times(0)).update(responseTime);
    }

    @Test
    public void testStop() {
        ReflectionTestUtils.setField(metrics, "metricsEnabled", true);

        metrics.stop();

        verify(reporter).report();
    }

    @Test
    public void testStopWhenDisabled() {
        ReflectionTestUtils.setField(metrics, "metricsEnabled", false);

        metrics.stop();

        verify(reporter, times(0)).report();
    }
}
