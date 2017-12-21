package kr.co.redbrush.bdd.test.metrics;

import kr.co.redbrush.bdd.test.ws.RestAssuredDriver;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class MetricsTest {
    @InjectMocks
    private Metrics metrics;


}
