package kr.co.redbrush.bdd.test.context;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class TestContextTest {
    private TestContext testContext = new TestContext();
    private String testKey = "testKey";
    private Integer testValue = 1;

    @Before
    public void before() {
        testContext.put(testKey, testValue);
    }

    @Test
    public void testGet() {
        Integer actualValue = testContext.get(testKey);

        assertThat("Unexpected value", actualValue, is(testValue));
    }

    @Test
    public void testGetWithTemplate() {
        Integer actualValue = testContext.get(testKey, Integer.class);

        assertThat("Unexpected value", actualValue, is(testValue));
    }

    @Test
    public void testGetWithNullValue() {
        Integer actualValue = testContext.get("tempKey");

        assertThat("Unexpected value", actualValue, nullValue());
    }

    @Test
    public void testPutAndRemove() {
        testContext.put(testKey, testValue);
        Integer actualValue = testContext.get(testKey);

        assertThat("Unexpected value", actualValue, is(testValue));

        testContext.remove(testKey);

        assertThat("Unexpected value", testContext.get(testKey), nullValue());
    }
}
