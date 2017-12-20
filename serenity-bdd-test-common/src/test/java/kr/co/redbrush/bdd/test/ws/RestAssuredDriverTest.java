package kr.co.redbrush.bdd.test.ws;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.ValidatableResponseLogSpec;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import kr.co.redbrush.bdd.test.exception.HttpMethodNotSpecifiedException;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest(SerenityRest.class)
public class RestAssuredDriverTest {
    @InjectMocks
    private RestAssuredDriver restAssuredDriver;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<Void> responseEntity;

    @Mock
    private WebServiceRequest request;

    @Mock
    private RequestSpecification requestSpec;

    @Mock
    private RequestLogSpecification requestLogSpec;

    @Mock
    private Response response;

    @Mock
    private ValidatableResponse validatableResponse;

    @Mock
    private ValidatableResponseLogSpec validatableResponseLogSpec;

    private String serverHost = "http://localhost:8080";
    private String warmUpEndpoint = "/";
    private String warmUpUrl = serverHost + warmUpEndpoint;
    private String testEndpoint = "/test";
    private String requestUrl = serverHost + testEndpoint;
    private HttpStatus httpStatus = HttpStatus.OK;
    private String contentType = "application/json";
    private String content = "content";
    private Map<String, Object> pathParameters = new HashMap<>();
    private Map<String, Object> queryParameters = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();
    private boolean urlEncodingEnabled = true;

    private String queryParamKey = "queryParam";
    private String queryParamValue = "queryParamValue";
    private String queryParamCollectionKey = "queryParamCollection";
    private List<String> queryParamCollectionValue = new ArrayList<>();

    @Before
    public void before() {
        pathParameters.put("pathParam", "123");
        queryParameters.put(queryParamKey, queryParamValue);
        queryParameters.put(queryParamCollectionKey, queryParamCollectionValue);
        headers.put("header", "header");

        when(restTemplate.getForEntity(warmUpUrl, Void.class)).thenReturn(responseEntity);
        when(responseEntity.getStatusCode()).thenReturn(httpStatus);

        PowerMockito.mockStatic(SerenityRest.class);
        when(SerenityRest.given()).thenReturn(requestSpec);
        when(requestSpec.log()).thenReturn(requestLogSpec);

        when(requestSpec.get(request.getUrl())).thenReturn(response);
        when(requestSpec.post(request.getUrl())).thenReturn(response);
        when(requestSpec.delete(request.getUrl())).thenReturn(response);
        when(requestSpec.put(request.getUrl())).thenReturn(response);
        when(response.then()).thenReturn(validatableResponse);
        when(validatableResponse.log()).thenReturn(validatableResponseLogSpec);
    }

    @Test
    public void testInit() {
        ReflectionTestUtils.setField(restAssuredDriver, "serverHost", serverHost);
        ReflectionTestUtils.setField(restAssuredDriver, "warmUpEndpoint", warmUpEndpoint);

        restAssuredDriver.init();

        assertThat("Unexpected statusCode", responseEntity.getStatusCode(), is(httpStatus));
    }

    @Test
    public void testInitWithoutHost() {
        restAssuredDriver.init();

        verify(restTemplate, times(0)).getForEntity(warmUpUrl, Void.class);
    }

    @Test(expected = HttpMethodNotSpecifiedException.class)
    public void testRequestWithoutMethod() {
        restAssuredDriver.request(request);
    }

    @Test
    public void testRequestWithAllParameters() {
        HttpMethod httpMethod = HttpMethod.GET;

        doReturn(httpMethod).when(request).getHttpMethod();
        when(request.getContentType()).thenReturn(contentType);
        when(request.getContent()).thenReturn(content);
        when(request.getPathParameters()).thenReturn(pathParameters);
        when(request.getQueryParameters()).thenReturn(queryParameters);
        when(request.getHeaders()).thenReturn(headers);
        when(request.isUrlEncodingEnabled()).thenReturn(urlEncodingEnabled);

        RestAssuredResponse restAssuredResponse = (RestAssuredResponse)restAssuredDriver.request(request);

        assertThat("Unexpected WebService Response.", restAssuredResponse.getResponse(), is(response));

        verify(requestSpec).contentType(request.getContentType());
        verify(requestSpec).body(request.getContent());
        verify(requestSpec).pathParams(request.getPathParameters());
        verify(requestSpec).queryParam(queryParamKey, queryParamValue);
        verify(requestSpec).queryParam(queryParamCollectionKey, queryParamCollectionValue);
        verify(requestSpec).headers(request.getHeaders());
        verify(requestSpec).urlEncodingEnabled(request.isUrlEncodingEnabled());

        validateRequestAndResponse();
    }

    @Test
    public void testGet() {
        testRequestByMethod(HttpMethod.GET);
    }

    @Test
    public void testPost() {
        testRequestByMethod(HttpMethod.POST);
    }

    @Test
    public void testDelete() {
        testRequestByMethod(HttpMethod.DELETE);
    }

    @Test
    public void testPut() {
        testRequestByMethod(HttpMethod.PUT);
    }

    private void testRequestByMethod(HttpMethod httpMethod) {
        when(request.getHttpMethod()).thenReturn(httpMethod);
        when(requestSpec.get(request.getUrl())).thenReturn(response);

        RestAssuredResponse restAssuredResponse = null;
        switch (httpMethod) {
            case GET:
                restAssuredResponse = (RestAssuredResponse) restAssuredDriver.get(request);
                break;
            case POST:
                restAssuredResponse = (RestAssuredResponse) restAssuredDriver.post(request);
                break;
            case DELETE:
                restAssuredResponse = (RestAssuredResponse) restAssuredDriver.delete(request);
                break;
            case PUT:
                restAssuredResponse = (RestAssuredResponse) restAssuredDriver.put(request);
                break;
        }

        assertThat("Unexpected WebService Response.", restAssuredResponse.getResponse(), is(response));

        validateRequestAndResponse(httpMethod);
    }

    private void validateRequestAndResponse() {
        validateRequestAndResponse(null);
    }

    private void validateRequestAndResponse(HttpMethod httpMethod) {
        if (httpMethod!=null) {
            verify(request).setHttpMethod(httpMethod);

            switch(httpMethod) {
                case GET:
                    verify(requestSpec).get(request.getUrl());
                    break;
                case POST:
                    verify(requestSpec).post(request.getUrl());
                    break;
                case DELETE:
                    verify(requestSpec).delete(request.getUrl());
                    break;
                case PUT:
                    verify(requestSpec).put(request.getUrl());
                    break;
            }
        }
        verify(requestSpec).log();
        verify(requestLogSpec).all();
        verify(response).then();
        verify(validatableResponseLogSpec).all();
    }
}
