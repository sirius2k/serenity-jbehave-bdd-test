package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class SpringWebServiceRequestBuilderFactoryTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private SpringWebServiceRequestBuilderFactory factory;
    private String serverHost = "http://localhost";
    private String defaultContentType = "application/json; charset=UTF-8";
    private boolean urlEncodingEnabled = false;
    private String path = "/";

    @Before
    public void before() throws Exception {
        createSpringWebServiceRequestBuilderFactory();
    }

    private void createSpringWebServiceRequestBuilderFactory() {
        factory = new SpringWebServiceRequestBuilderFactory() {
            @Override
            public WebServiceRequest.WebServiceRequestBuilder createInstance(Map<String, Object> parameters) {
                return null;
            }

            @Override
            public WebServiceRequest.WebServiceRequestBuilder createAuthorizedInstance(String username) {
                return null;
            }
        };
        factory.init();

        ReflectionTestUtils.setField(factory, "serverHost", serverHost);
        ReflectionTestUtils.setField(factory, "defaultContentType", defaultContentType);
        ReflectionTestUtils.setField(factory, "urlEncodingEnabled", false);
    }

    @Test
    public void testCreateInstance() {
        WebServiceRequest.WebServiceRequestBuilder requestBuilder = factory.createInstance();
        WebServiceRequest request = requestBuilder.build();
        request.setPath(path);

        assertThat("Value was not matched.", request.getUrl(), is(serverHost + path));
        assertThat("Value was not matched.", request.getContentType(), is(defaultContentType));
        assertThat("Value was not matched.", request.isUrlEncodingEnabled(), is(urlEncodingEnabled));
    }
}
