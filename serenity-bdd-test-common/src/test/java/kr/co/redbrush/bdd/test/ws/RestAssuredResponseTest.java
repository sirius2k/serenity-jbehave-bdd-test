package kr.co.redbrush.bdd.test.ws;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.xebialabs.restito.server.StubServer;
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
        String expectedAuthor = "Nigel Rees";

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String author = restAssuredResponse.getString(path);

        LOGGER.debug("Extracted author : {}", author);

        assertThat("Author was empty.", author, notNullValue());
        assertThat("Author was not matched.", author, is(expectedAuthor));
    }

    @Test
    public void testGetDefaultStringWithNullValue() {
        String path = "store.book[5].author";
        String defaultValue = "Default Author";
        String expectedValue = defaultValue;

        testGetDefaultString(path, defaultValue, expectedValue);
    }

    @Test
    public void testGetDefaultStringWithNotNullValue() {
        String path = "store.book[0].author";
        String defaultValue = "Default Author";
        String expectedValue = "Nigel Rees";

        testGetDefaultString(path, defaultValue, expectedValue);
    }

    private void testGetDefaultString(String path, String defaultValue, String expectedValue) {
        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        LOGGER.debug("path : {}, defaultValue : {}, expectedValue : {}", path, defaultValue, expectedValue);

        String author = restAssuredResponse.getDefaultString(path, defaultValue);

        assertThat("Author was empty.", author, notNullValue());
        assertThat("Author was not matched.", author, is(expectedValue));
    }

    @Test
    public void testGetInteger() {
        String path = "expensive";
        Integer expectedExpensive = 10;

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Integer expensive = restAssuredResponse.getInteger(path);

        LOGGER.debug("Extracted expensive : {}", expectedExpensive);

        assertThat("expensive was empty.", expensive, notNullValue());
        assertThat("expensive was not matched.", expensive, is(expectedExpensive));
    }

    @Test
    public void testGetFloat() {
        String path = "store.book[0].price";
        Float expectedPrice = 8.95f;

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Float price = restAssuredResponse.getFloat(path);

        LOGGER.debug("Extracted expensive : {}", expectedPrice);

        assertThat("price was empty.", price, notNullValue());
        assertThat("price was not matched.", price, is(expectedPrice));
    }

    @Test
    public void testGetDouble() {
        String path = "store.book[0].price";
        Double expectedPrice = 8.95d;

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Double price = restAssuredResponse.getDouble(path);

        LOGGER.debug("Extracted expensive : {}", expectedPrice);

        assertThat("price was empty.", price, notNullValue());
        assertThat("price was not matched.", price, is(expectedPrice));
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
    public void testGetObjectWithSpecificType() {
        Response response = expect().get(bookUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book expectedBook = createBook();
        Book book = restAssuredResponse.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetObjectWithNotExistingValue() {
        String path = "store.book[5].author";

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        restAssuredResponse.getObject(path);
    }

    @Test
    public void testGetObjectWithSpecificTypeAndPath() {
        String path = "store.book[0]";
        Book expectedBook = createBook();

        Response response = expect().get(storeUrl);
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObject(path, Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
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
