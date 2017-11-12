package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * Created by kwpark on 19/03/2017.
 */
@Slf4j
public abstract class SpringWebServiceRequestBuilderFactory implements WebServiceRequestBuilderFactory {
    @Value("${server.host}")
    protected String serverHost;

    @Value("${rest.assured.default.contentType:application/json; charset=UTF-8}")
    protected String defaultContentType;

    @Value("${rest.assured.default.urlEncodingEnabled:false}")
    protected boolean urlEncodingEnabled;

    @PostConstruct
    public void init() {
        LOGGER.debug("<Post Construct> ServerHost : {}, defaultContentType : {}", serverHost, defaultContentType);
    }

    @Override
    public WebServiceRequest.WebServiceRequestBuilder createInstance() {
        WebServiceRequest.WebServiceRequestBuilder builder = WebServiceRequest.builder()
                .serverHost(serverHost)
                .contentType(defaultContentType)
                .urlEncodingEnabled(urlEncodingEnabled);

        return builder;
    }
}
