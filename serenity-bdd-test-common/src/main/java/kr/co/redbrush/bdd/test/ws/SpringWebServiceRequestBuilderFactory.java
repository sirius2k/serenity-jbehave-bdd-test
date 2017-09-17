package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by kwpark on 19/03/2017.
 */
@Slf4j
public abstract class SpringWebServiceRequestBuilderFactory implements WebServiceRequestBuilderFactory {
    @Value("${server.host}")
    private String serverHost;

    @Value("${rest.assured.default.contentType:application/json; charset=UTF-8}")
    private String defaultContentType;

    @Value("${rest.assured.default.urlEncodingEnabled:false}")
    private boolean urlEncodingEnabled;

    @PostConstruct
    public void init() {
        LOGGER.debug("<Post Construct> ServerHost : {}, defaultContentType : {}", serverHost, defaultContentType);
    }

    public WebServiceRequest.WebServiceRequestBuilder createInstance() {
        WebServiceRequest.WebServiceRequestBuilder builder = WebServiceRequest.builder()
                .serverHost(serverHost)
                .contentType(defaultContentType)
                .urlEncodingEnabled(urlEncodingEnabled);

        return builder;
    }
}
