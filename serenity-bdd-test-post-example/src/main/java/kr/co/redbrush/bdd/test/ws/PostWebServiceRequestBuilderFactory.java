package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by kwpark on 19/03/2017.
 */
@Component
@Slf4j
public class PostWebServiceRequestBuilderFactory extends SpringWebServiceRequestBuilderFactory {

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

    @Override
    public WebServiceRequest.WebServiceRequestBuilder createInstance(Map<String, Object> parameters) {
        WebServiceRequest.WebServiceRequestBuilder builder = createInstance();

        // TODO : Do additional configuration with map

        return builder;
    }

    @Override
    public WebServiceRequest.WebServiceRequestBuilder createAuthorizedInstance(String username) {
        WebServiceRequest.WebServiceRequestBuilder builder = createInstance();

        // TODO : Do additional configuration with username

        return builder;
    }
}
