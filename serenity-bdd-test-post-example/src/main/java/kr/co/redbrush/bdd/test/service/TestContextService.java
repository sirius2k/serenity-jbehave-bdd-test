package kr.co.redbrush.bdd.test.service;

import kr.co.redbrush.bdd.test.context.TestContext;
import kr.co.redbrush.bdd.test.context.TestContextHolder;
import kr.co.redbrush.bdd.test.domain.User;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by kwpark on 13/03/2017.
 */
@Service
@Slf4j
public class TestContextService {

    public TestContext getTestContext() {
        return TestContextHolder.getContext();
    }

    public TestContext setLastResponse(WebServiceResponse response) {
        TestContext testContext = getTestContext();

        testContext.setLastResponse(response);

        return testContext;
    }

    public WebServiceResponse getLastResponse() {
        return getTestContext().getLastResponse();
    }

    public void clearContext() {
        Map<String, Object> map = getTestContext().getContextMap();
        Iterator<String> iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();

            if (key.startsWith("User")) {
                User user = (User)map.get(key);
                user.getSocketIOClient().close();
            }
        }

        TestContextHolder.clearContext();
    }

    public void setUser(String userWithIndex, User user) {
        getTestContext().put(userWithIndex, user);
    }

    public User getUser(String userWithIndex) {
        return (User)getTestContext().get(userWithIndex);
    }
}
