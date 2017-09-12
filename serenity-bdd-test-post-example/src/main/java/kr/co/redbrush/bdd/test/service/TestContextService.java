package kr.co.redbrush.bdd.test.service;

import kr.co.redbrush.bdd.test.context.TestContext;
import kr.co.redbrush.bdd.test.context.TestContextHolder;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        TestContextHolder.clearContext();
    }
}
