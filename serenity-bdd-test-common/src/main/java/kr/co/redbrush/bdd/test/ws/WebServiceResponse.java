package kr.co.redbrush.bdd.test.ws;

import org.hamcrest.Matcher;

/**
 * Created by kwpark on 18/04/2017.
 *
 * Object should return null instance of throwing exception when the value is not exists with given path.
 */
public interface WebServiceResponse {
    String getString(String path);
    String getDefaultString(String path, String defaultString);
    Integer getInteger(String path);
    Long getLong(String path);
    Float getFloat(String path);
    Double getDouble(String path);
    boolean isEmpty(String path);
    int getStatusCode();
    String getContentBody();
    <T> T getObject(String path);
    <T> T getObject(Class<T> clazz);
    <T> T getObject(String path, Class<T> clazz);
    <T> T getObjectByJsonPath(String path);
    <T> T getObjectByJsonPath(String path, Class<T> clazz);
    void bodyMatches(String var1, Matcher<?> var2, Object... var3);
    Long getResponseTime();
}
