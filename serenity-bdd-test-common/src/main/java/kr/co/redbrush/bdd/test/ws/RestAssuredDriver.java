package kr.co.redbrush.bdd.test.ws;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import kr.co.redbrush.bdd.test.exception.HttpMethodNotSpecifiedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.given;

/**
 * Created by kwpark on 19/03/2017.
 */
@Component
@Slf4j
public class RestAssuredDriver {
    @Value("${server.host}")
    protected String serverHost;

    @Value("${server.endpoint.warmup}")
    protected String warmUpEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        LOGGER.info("Warmup HTTP connection with dummy request prior to taking any measurements. Endpoint : {}{}", serverHost, warmUpEndpoint);

        if (StringUtils.isNotEmpty(serverHost) && StringUtils.isNotEmpty(warmUpEndpoint)) {
            ResponseEntity<Void> response  = restTemplate.getForEntity(serverHost + warmUpEndpoint, Void.class);

            LOGGER.info("Warmup result. Status Code : {}", response.getStatusCode());
        }
    }

    public WebServiceResponse get(WebServiceRequest request) {
        request.setHttpMethod(HttpMethod.GET);

        return sendRequest(request);
    }

    public WebServiceResponse post(WebServiceRequest request) {
        request.setHttpMethod(HttpMethod.POST);

        return sendRequest(request);
    }

    public WebServiceResponse delete(WebServiceRequest request) {
        request.setHttpMethod(HttpMethod.DELETE);

        return sendRequest(request);
    }

    public WebServiceResponse put(WebServiceRequest request) {
        request.setHttpMethod(HttpMethod.PUT);

        return sendRequest(request);
    }

    public WebServiceResponse request(WebServiceRequest request) {
        if (request.getHttpMethod()==null) {
            throw new HttpMethodNotSpecifiedException("HttpMethod was not specified.");
        }

        return sendRequest(request);
    }

    private WebServiceResponse sendRequest(WebServiceRequest request) {
        RequestSpecification requestSpec = createRequestSpecification(request);
        Response response = null;

        switch (request.getHttpMethod()) {
            case GET:
                response = requestSpec.get(request.getUrl());
                break;
            case POST:
                response = requestSpec.post(request.getUrl());
                break;
            case DELETE:
                response = requestSpec.delete(request.getUrl());
                break;
            case PUT:
                response = requestSpec.put(request.getUrl());
                break;
        }

        response.then().log().all();

        return new RestAssuredResponse(response);
    }

    private RequestSpecification createRequestSpecification(WebServiceRequest request) {
        RequestSpecification requestSpec = given();

        if (StringUtils.isNotEmpty(request.getContentType())) {
            requestSpec.contentType(request.getContentType());
        }

        if (request.getContent()!=null) {
            requestSpec.body(request.getContent());
        }

        if (MapUtils.isNotEmpty(request.getPathParameters())) {
            requestSpec.pathParams(request.getPathParameters());
        }

        if (MapUtils.isNotEmpty(request.getQueryParameters())) {
            Map<String, Object> queryMap = request.getQueryParameters();
            for (String key : queryMap.keySet()) {
                if (queryMap.get(key) instanceof Collection) {
                    requestSpec.queryParam(key, (Collection<?>) queryMap.get(key));
                } else {
                    requestSpec.queryParam(key, queryMap.get(key));
                }
            }
        }

        if (MapUtils.isNotEmpty(request.getHeaders())) {
            requestSpec.headers(request.getHeaders());
        }

        requestSpec.urlEncodingEnabled(request.isUrlEncodingEnabled());

        requestSpec.log().all();

        return requestSpec;
    }
}
