package kr.co.redbrush.bdd.test.ws;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import kr.co.redbrush.bdd.test.exception.HttpMethodNotSpecifiedException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static net.serenitybdd.rest.SerenityRest.given;

/**
 * Created by kwpark on 19/03/2017.
 */
@Component
public class RestAssuredDriver {

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

    private WebServiceResponse sendRequest(WebServiceRequest request) {
        RequestSpecification requestSpec = createRequestSpecification(request);
        Response response = null;

        if (request.getHttpMethod()==null) {
            throw new HttpMethodNotSpecifiedException("HttpMethod was not specified.");
        }

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

        return new RestAssurredResponse(response);
    }

    private RequestSpecification createRequestSpecification(WebServiceRequest request) {
        RequestSpecification requestSpec = given();

        if (StringUtils.isNotEmpty(request.getContentType())) {
            requestSpec.contentType(request.getContentType());
        }

        if (request.getContent()!=null) {
            requestSpec.content(request.getContent());
        }

        if (MapUtils.isNotEmpty(request.getPathParameters())) {
            requestSpec.pathParameters(request.getPathParameters());
        }

        if (MapUtils.isNotEmpty(request.getQueryParameters())) {
            requestSpec.queryParameters(request.getQueryParameters());
        }

        if (MapUtils.isNotEmpty(request.getHeaders())) {
            requestSpec.headers(request.getHeaders());
        }

        requestSpec.urlEncodingEnabled(request.isUrlEncodingEnabled());

        requestSpec.log().all();

        return requestSpec;
    }

}
