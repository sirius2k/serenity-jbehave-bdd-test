package kr.co.redbrush.bdd.test.ws;

import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;

import java.math.BigDecimal;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class RestAssuredResponse implements WebServiceResponse {
    private Response response;
    private PathType pathType = PathType.RESTASSURED;

    public RestAssuredResponse(Response response) {
        this.response = response;
    }

    public RestAssuredResponse(Response response, PathType pathType) {
        this.response = response;
        this.pathType = pathType;
    }

    @Override
    public String getString(String path) {
        return getDefaultString(path, null);
    }

    @Override
    public String getDefaultString(String path, String defaultString) {
        String value = getObject(path);

        if (StringUtils.isEmpty(value)) {
            value = defaultString;
        }

        return value;
    }

    @Override
    public Integer getInteger(String path) {
        return getObject(path);
    }

    @Override
    public Float getFloat(String path) {
        Float value = null;

        switch (pathType) {
            case RESTASSURED:
                value = getObject(path);
                break;
            case JSONPATH:
                value = BigDecimal.valueOf((Double)getObject(path)).floatValue();
                break;
        }
        return value;
    }

    @Override
    public Double getDouble(String path) {
        return getObject(path);
    }

    @Override
    public boolean isEmpty(String path) {
        return response.path(path)==null;
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public String getContentBody() {
        return response.getBody().asString();
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        T obj = null;

        switch (pathType) {
            case JSONPATH:
                obj = JsonPath.parse(getContentBody()).read("$", clazz);
                break;
            case RESTASSURED:
                obj = response.as(clazz);
                break;
        }

        return obj;
    }

    @Override
    public <T> T getObject(String path) {
        T obj = null;

        switch (pathType) {
            case JSONPATH:
                obj = JsonPath.read(getContentBody(), path);
                break;
            case RESTASSURED:
                obj = response.path(path);
                break;
        }

        return obj;
    }

    @Override
    public void bodyMatches(String var1, Matcher<?> var2, Object... var3) {
        response.then().body(var1, var2, var3);
    }
}
