package kr.co.redbrush.bdd.test.context;

import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * Created by kwpark on 20/03/2017.
 */

@Data
public class TestContext {
    private LinkedHashMap<String, Object> contextMap = new LinkedHashMap<String, Object>();
    private WebServiceResponse lastResponse;

    public <T> T get(String key) {
        return (T)contextMap.get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return (T)contextMap.get(key);
    }

    public void put(String key, Object obj) {
        contextMap.put(key, obj);
    }

    public void remove(String key) {
        contextMap.remove(key);
    }
}
