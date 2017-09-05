package kr.co.redbrush.bdd.test.ws;

import org.hamcrest.Matcher;

/**
 * Created by kwpark on 18/04/2017.
 */
public interface WebServiceResponse {
    String getString(String path);
    String getDefaultString(String path, String defaultString);
    Integer getInteger(String path);
    Float getFloat(String path);
    Double getDouble(String path);
    boolean isEmpty(String path);
    int getStatusCode();
    <T> T getObject(Class<T> clazz);
    Object getObject(String path);
    void bodyMatches(String var1, Matcher<?> var2, Object... var3);
}
