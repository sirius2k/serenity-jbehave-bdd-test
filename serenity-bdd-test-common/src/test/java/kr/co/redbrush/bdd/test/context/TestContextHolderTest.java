package kr.co.redbrush.bdd.test.context;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
@Slf4j
public class TestContextHolderTest {
    @Test
    public void testGetContextHolder() {
        TestContext testContext = TestContextHolder.getContext();

        LOGGER.debug("testContext : {}", testContext);

        assertThat("TestContext shouldn't be null", testContext, notNullValue());
    }

    @Test
    public void testCreateEmptyContext() {
        TestContext testContext = TestContextHolder.createEmptyContext();

        LOGGER.debug("testContext : {}", testContext);

        assertThat("TestContext shouldn't be null", testContext, notNullValue());
    }

    @Test
    public void testClearContext() {
        LOGGER.debug("testContextHolder : {}", TestContextHolder.getContextHolder());

        TestContext testContext = TestContextHolder.getContext();
        assertThat("TestContext shouldn't be empty", TestContextHolder.getContextHolder().get(), notNullValue());

        TestContextHolder.clearContext();
        assertThat("TestContext should be empty", TestContextHolder.getContextHolder().get(), nullValue());
    }
}
