package kr.co.redbrush.bdd.test.ws;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.json.JSONObject;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class SocketIOResponse implements WebServiceResponse {
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Getter
    private JSONObject json;

    private Object document;

    public SocketIOResponse(JSONObject json) {
        this.json = json;

        document = Configuration.defaultConfiguration().jsonProvider().parse(json.toString());
    }

    @Override
    public String getString(String path) {
        return getDefaultString(path, null);
    }

    @Override
    public String getDefaultString(String path, String defaultString) {
        String value = defaultString;

        try {
            value = JsonPath.read(document, path);
        } catch (Exception e) {
            LOGGER.error("Can't get string from json. Path : {}", path, e);
        }

        return value;
    }

    @Override
    public Integer getInteger(String path) {
        return getObject(path, Integer.class);
    }

    @Override
    public Long getLong(String path) {
        return getObject(path, Long.class);
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
            obj = JsonPath.read(document, path);
        } catch (Exception e) {
            LOGGER.error("Cannot get object. path={}, json=\n{}", path, getContentBody(), e);
        }

        return obj==null ? true : false;
    }

    @Override
    public int getStatusCode() {
        // There's no status code in SocketIO
        return 0;
    }

    @Override
    public String getContentBody() {
        return json.toString();
    }

    @Override
    public <T> T getObject(String path) {
        T obj = null;

        try {
            obj = JsonPath.read(document, path);
        } catch (Exception e) {
            LOGGER.error("Cannot get object. path={}, json=\n{}", path, getContentBody(), e);
        }

        return obj;
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        T obj = null;

        try {
            obj = mapper.convertValue(JsonPath.read(document, "$"), clazz);
        } catch (Exception e) {
            LOGGER.error("Cannot get object. Class={}, json=\n{}", clazz, getContentBody(), e);
        }

        return obj;
    }

    @Override
    public <T> T getObject(String path, Class<T> clazz) {
        T obj = null;

        try {
            obj = mapper.convertValue(JsonPath.read(document, path), clazz);
        } catch (Exception e) {
            LOGGER.info("Cannot get object. Class={}", clazz, getContentBody());
        }

        return obj;
    }

    @Override
    public <T> T getObjectByJsonPath(String path) {
        return JsonPath.read(document, path);
    }

    @Override
    public <T> T getObjectByJsonPath(String path, Class<T> clazz) {
        return mapper.convertValue(JsonPath.read(document, path), clazz);
    }

    @Override
    public void bodyMatches(String var1, Matcher<?> var2, Object... var3) {
        var2.matches(var1);
        var2.matches(var3);
    }

    @Override
    public Long getResponseTime() {
        // We can't measure response time of socket io interactions.
        return 0L;
    }
}
