package kr.co.redbrush.bdd.test.ws;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.xebialabs.restito.server.StubServer;
import kr.co.redbrush.bdd.test.ws.helper.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.expect;
import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.*;
import static com.xebialabs.restito.semantics.Condition.get;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by kwpark on 18/04/2017.
 *
 * Test of RestAssuredResponse which used GPath : http://groovy-lang.org/processing-xml.html#_gpath
 */
@Slf4j
public class RestAssuredResponseTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private StubServer server;
    private String bookStoreJson;
    private String bookJson;
    private String contentType = "application/json";
    private String storeUrl = "/store";
    private String bookUrl = "/book";

    @Before
    public void before() throws Exception {
        readBookStoreJsonExample();
        readBookJsonExample();

        server = new StubServer().run();
        RestAssured.port = server.getPort();

        whenHttp(server)
                .match(get(storeUrl))
                .then(
                        ok(),
                        contentType(contentType),
                        stringContent(bookStoreJson)
                );

        whenHttp(server)
                .match(get(bookUrl))
                .then(
                        ok(),
                        contentType(contentType),
                        stringContent(bookJson)
                );

    }

    private void readBookStoreJsonExample() throws IOException {
        InputStream ios = this.getClass().getResourceAsStream("/bookstore.json");
        bookStoreJson = IOUtils.toString(ios, "UTF-8");
        IOUtils.closeQuietly(ios);
    }

    private void readBookJsonExample() throws IOException {
        InputStream ios = this.getClass().getResourceAsStream("/book.json");
        bookJson = IOUtils.toString(ios, "UTF-8");
        IOUtils.closeQuietly(ios);
    }

    @After
    public void after() throws Exception {
        server.stop();
    }

    @Test
    public void testGetString() {
        String path = "store.book[0].author";
        String expectedValue = "Nigel Rees";

        testGetString(path, expectedValue);
    }

    @Test
    public void testGetStringWithInvalidPath() {
        String path = "store.book[5].author";
        String expectedValue = null;

        testGetString(path, expectedValue);
    }

    private void testGetString(String path, String expectedValue) {
        LOGGER.debug("path : {}, expectedValue value : {}", path, expectedValue);

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String actualValue = restAssuredResponse.getString(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test
    public void testGetDefaultString() {
        String path = "store.book[0].author";
        String defaultValue = "Default Author";
        String expectedValue = "Nigel Rees";

        testGetDefaultString(path, defaultValue, expectedValue);
    }

    @Test
    public void testGetDefaultStringWithInvalidPath() {
        String path = "store.book[5].author";
        String defaultValue = "Default Author";
        String expectedValue = defaultValue;

        testGetDefaultString(path, defaultValue, expectedValue);
    }


    private void testGetDefaultString(String path, String defaultValue, String expectedValue) {
        LOGGER.debug("path : {}, defaultValue : {}, expectedValue : {}", path, defaultValue, expectedValue);

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        String actualValue = restAssuredResponse.getDefaultString(path, defaultValue);

        assertThat("Value was empty.", actualValue, notNullValue());
        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test
    public void testGetInteger() {
        String path = "expensive";
        Integer expectedValue = 10;

        testGetInteger(path, expectedValue);
    }

    @Test
    public void testGetIntegerWithInvalidPath() {
        String path = "expen";
        Integer expectedValue = null;

        testGetInteger(path, expectedValue);
    }
    
    private void testGetInteger(String path, Integer expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Integer actualValue = restAssuredResponse.getInteger(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test
    public void testGetFloat() {
        String path = "store.book[0].price";
        Float expectedValue = 8.95f;

        testGetFloat(path, expectedValue);
    }

    @Test
    public void testGetFloatWithInvalidPath() {
        String path = "store.book[5].price";
        Float expectedValue = null;

        testGetFloat(path, expectedValue);
    }

    private void testGetFloat(String path, Float expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Float actualValue = restAssuredResponse.getFloat(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test
    public void testGetDouble() {
        String path = "store.book[0].price";
        Double expectedValue = 8.95d;

        testGetDouble(path, expectedValue);
    }

    @Test
    public void testGetDoubleWithInvalidPath() {
        String path = "store.book[5].price";
        Double expectedValue = null;

        testGetDouble(path, expectedValue);
    }

    private void testGetDouble(String path, Double expectedValue) {
        LOGGER.debug("path : {}, expectedValue : {}", path, expectedValue);

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Double actualValue = restAssuredResponse.getDouble(path);

        assertThat("Value was not matched.", actualValue, is(expectedValue));
    }

    @Test
    public void testGetObject() {
        String path = "store.book.author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        List<String> authors = restAssuredResponse.getObject(path);

        LOGGER.debug("Extracted authors : {}", authors);

        assertThat("Authors was empty.", authors, notNullValue());
        assertThat("Authors was not matched.", authors, containsInAnyOrder(expectedAuthors.toArray()));
    }

    @Test
    public void testGetObjectWithInvalidPath() {
        String path = "store.book[5].author";

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String value = restAssuredResponse.getObject(path);
        String expectedValue = null;

        assertThat("Value was not matched.", value, is(expectedValue));
    }

    @Test
    public void testGetObjectWithSpecificType() {
        Response response = expect().get(bookUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book expectedBook = createBook();
        Book book = restAssuredResponse.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test
    public void testGetObjectWithSpecificTypeAndInvalidClass() {
        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, nullValue());
    }

    @Test
    public void testGetObjectWithPathAndSpecificType() {
        String path = "store.book[0]";
        Book expectedBook = createBook();

        testGetObjectWithSpecificTypeAndPath(path, Book.class, expectedBook);
    }

    @Test
    public void testGetObjectWithPathAndSpecificTypeAndInvalidPath() {
        String path = "store.book[5]";
        Book expectedValue = null;

        testGetObjectWithSpecificTypeAndPath(path, Book.class, expectedValue);
    }

    private void testGetObjectWithSpecificTypeAndPath(String path, Class clazz, Object expectedValue) {
        LOGGER.debug("Path : {}, Class : {}", path, clazz);

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObject(path, Book.class);

        if (expectedValue == null) {
            assertThat("Book was not matched.", book, is(expectedValue));
        } else {
            assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedValue, book), is(true));
        }
    }

    private Book createBook() {
        Book book = new Book();
        book.setCategory("reference");
        book.setAuthor("Nigel Rees");
        book.setTitle("Sayings of the Century");
        book.setPrice(Float.valueOf(8.95f));

        return book;
    }

    @Test
    public void testGetObjectByJsonPath() {
        String path = "$.store.book[*].author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        List<String> authors = restAssuredResponse.getObjectByJsonPath(path);

        LOGGER.debug("Extracted authors : {}", authors);

        assertThat("Authors was empty.", authors, notNullValue());
        assertThat("Authors was not matched.", authors, containsInAnyOrder(expectedAuthors.toArray()));
    }

    @Test
    public void testGetObjectByJsonPathWithSpecificType() {
        String path = "$.store.book[0]";
        Book expectedBook = createBook();

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObjectByJsonPath(path, Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test
    public void testIsEmpty() {
        String path1 = "store.book[0].author";
        String path2 = "store.book[5].author";
        String expectedAuthor = "Nigel Rees";

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        boolean empty1 = restAssuredResponse.isEmpty(path1);
        boolean empty2 = restAssuredResponse.isEmpty(path2);

        assertThat("Author should not be empty.", empty1, is(false));
        assertThat("Author should be empty.", empty2, is(true));
    }

    @Test
    public void testGetStatusCode() {
        int expectedStatusCode = 200;
        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        int statusCode = restAssuredResponse.getStatusCode();

        assertThat("Status code was not matched.", statusCode, is(expectedStatusCode));
    }

    @Test
    public void testGetContentBody() {
        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        String contentBody = restAssuredResponse.getContentBody();

        assertThat("ContentBody was not matched.", contentBody, is(bookStoreJson));
    }

    @Test
    public void testBodyMatches() {
        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String var1 = "store.book[0]";
        Matcher var2 = null;
        Object var3 = null;

        restAssuredResponse.bodyMatches("store.book[0].author", equalTo("Nigel Rees"));
    }
}
