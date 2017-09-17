package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by kwpark on 19/03/2017.
 */
public interface WebServiceRequestBuilderFactory {
    WebServiceRequest.WebServiceRequestBuilder createInstance();
    WebServiceRequest.WebServiceRequestBuilder createInstance(Map<String, Object> parameters);
    WebServiceRequest.WebServiceRequestBuilder createAuthorizedInstance(String username);
}
