package kr.co.redbrush.bdd.test.ws;

import com.jayway.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;

/**
 * Created by kwpark on 18/04/2017.
 */
public class RestAssurredResponse implements WebServiceResponse {
    private Response response;

    public RestAssurredResponse(Response response) {
        this.response = response;
    }

    @Override
    public String getString(String path) {
        return getDefaultString(path, null);
    }

    @Override
    public String getDefaultString(String path, String defaultString) {
        String value = response.path(path);

        if (StringUtils.isEmpty(value) && StringUtils.isEmpty(defaultString)) {
            value = defaultString;
        }

        return value;
    }

    @Override
    public Integer getInteger(String path) {
        Integer value = response.path(path);

        return value;
    }

    @Override
    public Float getFloat(String path) {
        Float value = response.path(path);

        return value;
    }

    @Override
    public Double getDouble(String path) {
        Double value = response.path(path);

        return value;
    }

    @Override
    public boolean isEmpty(String path) {
        Object value = response.path(path);

        return value!=null ? true : false;
    }

    @Override
    public int getStatusCode() {
        return response.getStatusCode();
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        return response.as(clazz);
    }

    @Override
    public Object getObject(String path) {
        return response.path(path);
    }

    @Override
    public void bodyMatches(String var1, Matcher<?> var2, Object... var3) {
        response.then().body(var1, var2, var3);
    }
}
