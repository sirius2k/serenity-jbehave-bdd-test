package kr.co.redbrush.bdd.test.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@Slf4j
public class MetricsTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private Metrics metrics;

    @Mock
    private MetricRegistry metricRegistry;

    @Mock
    private Histogram histogram;

    @Mock
    private HtmlAggregateReporter reporter;

    private String registerName = "test_metric";
    private Long responseTime = 100L;

    @Before
    public void before() {
        when(metricRegistry.histogram(registerName)).thenReturn(histogram);
    }

    @Test
    public void testUpdateMetrics() {
        ReflectionTestUtils.setField(metrics, "metricsEnabled", true);

        metrics.updateMetric(registerName, responseTime);

        verify(histogram).update(responseTime);
    }

    @Test
    public void testUpdateMetricsWithMetricsDisabled() {
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
    public void testStopWithMetricsDisabled() {
        ReflectionTestUtils.setField(metrics, "metricsEnabled", false);

        metrics.stop();

        verify(reporter, times(0)).report();
    }
}
