package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import kr.co.redbrush.bdd.test.ws.helper.Book;
import kr.co.redbrush.bdd.test.ws.helper.SocketIOServerSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by kwpark on 18/04/2017.
 *
 * Test of RestAssuredResponse which used GPath : http://groovy-lang.org/processing-xml.html#_gpath
 */
@Slf4j
public class SocketIOResponseTest extends SocketIOServerSupport {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Socket socket;
    private SocketIOClient socketIOClient;

    @Before
    public void before() throws Exception {
        socket = client();
        socketIOClient = new SocketIOClient(socket) {
            @Override
            public void bindCustomEmitterListeners() {
            }
        };
        socketIOClient.connect();

        socketIOClient.emit("getBookStore");
    }

    @After
    public void after() throws Exception {
        socketIOClient.disconnect();
    }

    private JSONObject getBookStore() {
        return socketIORequest("getBookStore");
    }

    private JSONObject getBook() {
        return socketIORequest("getBook");
    }

    private JSONObject socketIORequest(String event) {
        DefaultEmitterListener listener = new DefaultEmitterListener(socketIOClient);

        socketIOClient.bindEvent(event, listener);
        socketIOClient.emit(event);

        JSONObject response = socketIOClient.waitJsonMessage();

        return response;
    }

    @Test(timeout = TIMEOUT)
    public void testGetString() {
        String path = "$.store.book[0].author";
        String expectedValue = "Nigel Rees";

        testGetString(path, expectedValue);
    }

    @Test(timeout = TIMEOUT)
    public void testGetStringWithInvalidPath() {
        String path = "$.store.book[5].author";
        String expectedValue = null;

        testGetString(path, expectedValue);
    }

    private void testGetString(String path, String expectedValue) {
        LOGGER.debug("path : {}, expectedValue value : {}", path, expectedValue);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        String actualValue = response.getString(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetDefaultString() {
        String path = "store.book[0].author";
        String defaultValue = "Default Author";
        String expectedValue = "Nigel Rees";

        testGetDefaultString(path, defaultValue, expectedValue);
    }

    @Test(timeout = TIMEOUT)
    public void testGetDefaultStringWithInvalidPath() {
        String path = "store.book[5].author";
        String defaultValue = "Default Author";
        String expectedValue = defaultValue;

        testGetDefaultString(path, defaultValue, expectedValue);
    }

    private void testGetDefaultString(String path, String defaultValue, String expectedValue) {
        LOGGER.debug("path : {}, defaultValue : {}, expectedValue : {}", path, defaultValue, expectedValue);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        String actualValue = response.getDefaultString(path, defaultValue);

        assertThat("Value was empty.", actualValue, notNullValue());
        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetInteger() {
        String path = "$.expensive";
        Integer expectedValue = 10;

        testGetInteger(path, expectedValue);
    }

    @Test(timeout = TIMEOUT)
    public void testGetIntegerWithInvalidPath() {
        String path = "$.expen";
        Integer expectedValue = null;

        testGetInteger(path, expectedValue);
    }
    
    private void testGetInteger(String path, Integer expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Integer actualValue = response.getInteger(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetLong() {
        String path = "expensive";
        Long expectedValue = 10L;

        testGetLong(path, expectedValue);
    }

    @Test(timeout = TIMEOUT)
    public void testGetLongWithInvalidPath() {
        String path = "expen";
        Long expectedValue = null;

        testGetLong(path, expectedValue);
    }

    private void testGetLong(String path, Long expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Long actualValue = response.getLong(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetFloat() {
        String path = "store.book[0].price";
        Float expectedValue = 8.95f;

        testGetFloat(path, expectedValue);
    }

    @Test(timeout = TIMEOUT)
    public void testGetFloatWithInvalidPath() {
        String path = "store.book[5].price";
        Float expectedValue = null;

        testGetFloat(path, expectedValue);
    }

    private void testGetFloat(String path, Float expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Float actualValue = response.getFloat(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetDouble() {
        String path = "store.book[0].price";
        Double expectedValue = 8.95d;

        testGetDouble(path, expectedValue);
    }

    @Test(timeout = TIMEOUT)
    public void testGetDoubleWithInvalidPath() {
        String path = "store.book[5].price";
        Double expectedValue = null;

        testGetDouble(path, expectedValue);
    }

    private void testGetDouble(String path, Double expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Double actualValue = response.getDouble(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetObject() {
        String path = "$.store.book[*].author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        List<String> authors = response.getObject(path);

        LOGGER.debug("Extracted authors : {}", authors);

        assertThat("Authors was empty.", authors, notNullValue());
        assertThat("Authors was not matched.", authors, containsInAnyOrder(expectedAuthors.toArray()));
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectWithInvalidPath() {
        String path = "$.store.book[5].author";

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        String value = response.getObject(path);
        String expectedValue = null;

        assertThat("Value was not matched.", value, is(expectedValue));
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectWithSpecificType() {
        JSONObject bookJson = getBook();
        SocketIOResponse response = new SocketIOResponse(bookJson);
        Book expectedBook = createBook();
        Book book = response.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectWithSpecificTypeAndInvalidClass() {
        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Book book = response.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", book.getAuthor(), nullValue());
        assertThat("Book was not matched.", book.getCategory(), nullValue());
        assertThat("Book was not matched.", book.getPrice(), nullValue());
        assertThat("Book was not matched.", book.getTitle(), nullValue());
    }

    private Book createBook() {
        Book book = new Book();
        book.setCategory("reference");
        book.setAuthor("Nigel Rees");
        book.setTitle("Sayings of the Century");
        book.setPrice(Float.valueOf(8.95f));

        return book;
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectWithPathAndSpecificType() {
        String path = "$.store.book[0]";
        Book expectedBook = createBook();

        testGetObjectWithSpecificTypeAndPath(path, Book.class, expectedBook);
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectWithPathAndSpecificTypeAndInvalidPath() {
        String path = "$.store.book[5]";
        Book expectedValue = null;

        testGetObjectWithSpecificTypeAndPath(path, Book.class, expectedValue);
    }

    private void testGetObjectWithSpecificTypeAndPath(String path, Class clazz, Object expectedValue) {
        LOGGER.debug("Path : {}, Class : {}", path, clazz);

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Book book = response.getObject(path, Book.class);

        if (expectedValue == null) {
            assertThat("Book was not matched.", book, is(expectedValue));
        } else {
            assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedValue, book), is(true));
        }
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectByJsonPath() {
        String path = "$.store.book[*].author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        List<String> authors = response.getObjectByJsonPath(path);

        LOGGER.debug("Extracted authors : {}", authors);

        assertThat("Authors was empty.", authors, notNullValue());
        assertThat("Authors was not matched.", authors, containsInAnyOrder(expectedAuthors.toArray()));
    }

    @Test(timeout = TIMEOUT)
    public void testGetObjectByJsonPathWithSpecificType() {
        String path = "$.store.book[0]";
        Book expectedBook = createBook();

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        Book book = response.getObjectByJsonPath(path, Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test(timeout = TIMEOUT)
    public void testIsEmpty() {
        String path1 = "$.store.book[0].author";
        String path2 = "$.store.book[5].author";
        String expectedAuthor = "Nigel Rees";

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);

        boolean empty1 = response.isEmpty(path1);
        boolean empty2 = response.isEmpty(path2);

        assertThat("Author should not be empty.", empty1, is(false));
        assertThat("Author should be empty.", empty2, is(true));
    }

    @Test(timeout = TIMEOUT)
    public void testIsEmptyThrowsException() {
        String path1 = "$.store.book[0].author.test";
        String path2 = "$.store.book[5].author.test";
        String expectedAuthor = "Nigel Rees";

        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);

        boolean empty1 = response.isEmpty(path1);
        boolean empty2 = response.isEmpty(path2);

        assertThat("Author should be empty.", empty1, is(true));
        assertThat("Author should be empty.", empty2, is(true));
    }

    @Test(timeout = TIMEOUT)
    public void testGetStatusCode() {
        int expectedStatusCode = 0;
        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);

        int statusCode = response.getStatusCode();

        assertThat("Status code was not matched.", statusCode, is(expectedStatusCode));
    }

    @Test(timeout = TIMEOUT)
    public void testGetContentBody() {
        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);

        String contentBody = response.getContentBody();

        assertThat("ContentBody was not matched.", contentBody, is(bookStoreJson.toString()));
    }

    @Test(timeout = TIMEOUT)
    public void testBodyMatches() {
        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);
        String var1 = "$.store.book[0]";
        Matcher var2 = null;
        Object var3 = null;

        response.bodyMatches("$.store.book[0].author", equalTo("Nigel Rees"));
    }

    @Test(timeout = TIMEOUT)
    public void testGetResponseTime() {
        JSONObject bookStoreJson = getBookStore();
        SocketIOResponse response = new SocketIOResponse(bookStoreJson);

        Long responseTime = response.getResponseTime();

        LOGGER.debug("Response time : {}", responseTime);

        assertThat("Unexpected response time.", responseTime, notNullValue());
    }
}
