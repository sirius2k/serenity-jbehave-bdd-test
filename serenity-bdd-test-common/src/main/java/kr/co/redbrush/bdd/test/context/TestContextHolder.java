package kr.co.redbrush.bdd.test.context;

/**
 * Created by kwpark on 20/03/2017.
 */
public class TestContextHolder {
    private static final ThreadLocal<TestContext> contextHolder = new ThreadLocal<TestContext>();

    /**
     * Explicitly clears the context value from the current thread
     */
    public static void clearContext() {
        contextHolder.remove();
    }

    public static TestContext getContext() {
        TestContext context = contextHolder.get();

        if (context == null) {
            context = createEmptyContext();
            contextHolder.set(context);
        }

        return context;
    }

    public static TestContext createEmptyContext() {
        return new TestContext();
    }

    public static ThreadLocal<TestContext> getContextHolder() { return contextHolder; }
}
