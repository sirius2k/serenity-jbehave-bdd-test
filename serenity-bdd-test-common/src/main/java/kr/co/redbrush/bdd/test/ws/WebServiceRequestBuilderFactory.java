package kr.co.redbrush.bdd.test.ws;

import java.util.Map;

/**
 * Created by kwpark on 19/03/2017.
 */
public interface WebServiceRequestBuilderFactory {
    WebServiceRequest.WebServiceRequestBuilder createInstance();
    WebServiceRequest.WebServiceRequestBuilder createInstance(Map<String, Object> parameters);
    WebServiceRequest.WebServiceRequestBuilder createAuthorizedInstance(String username);
}
