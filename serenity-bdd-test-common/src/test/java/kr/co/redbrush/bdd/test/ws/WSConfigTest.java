package kr.co.redbrush.bdd.test.ws;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class WSConfigTest {
    private WSConfig wsConfig = new WSConfig();

    @Test
    public void testRestTemplate() {
        RestTemplate restTemplate = wsConfig.restTemplate();

        assertThat("Unexpected restTemplate.", restTemplate, notNullValue());
    }
}
