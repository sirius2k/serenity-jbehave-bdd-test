package kr.co.redbrush.bdd.test.ws;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class RestAssuredResponse implements WebServiceResponse {
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Response response;

    public RestAssuredResponse(Response response) {
        this.response = response;
    }

    @Override
    public String getString(String path) {
        return getDefaultString(path, null);
    }

    @Override
    public String getDefaultString(String path, String defaultString) {
        String value = null;

        try {
            value = getObject(path);
        } catch(Exception e) {
            LOGGER.error("Cannot get object. path : {}, Response : {}", path, getContentBody());
            LOGGER.error("Cannot get object.", e);
        }

        if (StringUtils.isEmpty(value)) {
            value = defaultString;
        }

        return value;
    }

    @Override
    public Integer getInteger(String path) {
        return getObject(path, Integer.class);
    }

    @Override
    public Float getFloat(String path) {
        return getObject(path, Float.class);
    }

    @Override
    public Double getDouble(String path) {
        return getObject(path, Double.class);
    }

    @Override
    public boolean isEmpty(String path) {
        Object obj = null;

        try {
            obj = response.path(path);
        } catch (Exception e) {
            LOGGER.info("Cannot get object. path={}", path);
        }

        return obj==null ? true : false;
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
    public <T> T getObject(String path) {
        return response.path(path);
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        return response.as(clazz);
    }

    @Override
    public <T> T getObject(String path, Class<T> clazz) {
        return response.jsonPath().getObject(path, clazz);
    }

    @Override
    public <T> T getObjectByJsonPath(String path) {
        return JsonPath.read(getContentBody(), path);
    }

    @Override
    public <T> T getObjectByJsonPath(String path, Class<T> clazz) {
        return mapper.convertValue(JsonPath.read(getContentBody(), path), clazz);
    }

    @Override
    public void bodyMatches(String var1, Matcher<?> var2, Object... var3) {
        response.then().body(var1, var2, var3);
    }
}
