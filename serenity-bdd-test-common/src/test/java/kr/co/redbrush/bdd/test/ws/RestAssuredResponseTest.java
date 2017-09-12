package kr.co.redbrush.bdd.test.ws;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class RestAssuredResponseTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public Response response;

    @Mock
    public ValidatableResponse validatableResponse;

    @Mock
    public ResponseBody responseBody;

    @Mock
    public JsonPath jsonPath;

    private String bookStoreJson;
    private String bookJson;

    @Before
    public void before() throws Exception {
        readBookStoreJsonExample();
        readBookJsonExample();

        when(response.getBody()).thenReturn(responseBody);
        when(response.then()).thenReturn(validatableResponse);
        when(response.jsonPath()).thenReturn(jsonPath);
        when(responseBody.asString()).thenReturn(bookStoreJson);
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

    @Test
    public void testGetString() {
        String path = "$.store.book[0].author";
        String expectedAuthor = "Nigel Rees";

        when(response.path(path)).thenReturn(expectedAuthor);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String author = restAssuredResponse.getString(path);

        LOGGER.debug("Extracted author : {}", author);

        assertThat("Author was empty.", author, notNullValue());
        assertThat("Author was not matched.", author, is(expectedAuthor));
    }

    @Test
    public void testGetDefaultStringWithNullValue() {
        String path = "$.store.book[0].author";
        String extractedValue = null;
        String defaultValue = "Default Author";

        testGetDefaultString(path, extractedValue, defaultValue);
    }

    @Test
    public void testGetDefaultStringWithNotNullValue() {
        String path = "$.store.book[0].author";
        String extractedValue = "Test Author";
        String defaultValue = "Default Author";

        testGetDefaultString(path, extractedValue, defaultValue);
    }

    private void testGetDefaultString(String path, String extractedValue, String defaultValue) {
        when(response.path(path)).thenReturn(extractedValue);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String author = restAssuredResponse.getDefaultString(path, defaultValue);

        LOGGER.debug("Extracted value : {}, defaultValue : {}", extractedValue, defaultValue);

        assertThat("Author was empty.", author, notNullValue());

        if (extractedValue == null) {
            assertThat("Author was not matched.", author, is(defaultValue));
        } else {
            assertThat("Author was not matched.", author, is(extractedValue));
        }
    }

    @Test
    public void testGetInteger() {
        String path = "$.expensive";
        Integer expectedExpensive = 10;

        when(response.path(path)).thenReturn(expectedExpensive);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Integer expensive = restAssuredResponse.getInteger(path);

        LOGGER.debug("Extracted expensive : {}", expectedExpensive);

        assertThat("expensive was empty.", expensive, notNullValue());
        assertThat("expensive was not matched.", expensive, is(expectedExpensive));
    }

    @Test
    public void testGetFloat() {
        String path = "$.store.book[0].price";
        Float expectedPrice = 8.95f;

        when(response.path(path)).thenReturn(expectedPrice);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Float price = restAssuredResponse.getFloat(path);

        LOGGER.debug("Extracted expensive : {}", expectedPrice);

        assertThat("price was empty.", price, notNullValue());
        assertThat("price was not matched.", price, is(expectedPrice));
    }

    @Test
    public void testGetDouble() {
        String path = "$.store.book[0].price";
        Double expectedPrice = 8.95d;

        when(response.path(path)).thenReturn(expectedPrice);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Double price = restAssuredResponse.getDouble(path);

        LOGGER.debug("Extracted expensive : {}", expectedPrice);

        assertThat("price was empty.", price, notNullValue());
        assertThat("price was not matched.", price, is(expectedPrice));
    }

    @Test
    public void testGetObject() {
        String path = "$.store.book[*].author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

        when(response.path(path)).thenReturn(expectedAuthors);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        List<String> authors = restAssuredResponse.getObject(path);

        LOGGER.debug("Extracted authors : {}", authors);

        assertThat("Authors was empty.", authors, notNullValue());
        assertThat("Authors was not matched.", authors, containsInAnyOrder(expectedAuthors.toArray()));
    }

    @Test
    public void testGetObjectWithSpecificType() {
        Book expectedBook = createBook();

        when(response.as(Book.class)).thenReturn(expectedBook);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test
    public void testGetObjectWithSpecificTypeAndPath() {
        String path = "$.store.book[0]";
        Book expectedBook = createBook();

        when(jsonPath.getObject(path, Book.class)).thenReturn(expectedBook);

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObject(path, Book.class);

        LOGGER.debug("Extracted book : {}", book);

        assertThat("Book was empty.", book, notNullValue());
        assertThat("Book was not matched.", EqualsBuilder.reflectionEquals(expectedBook, book), is(true));
    }

    @Test
    public void testGetObjectByJsonPath() {
        String path = "$.store.book[*].author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

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

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        Book book = restAssuredResponse.getObjectByJsonPath(path, Book.class);

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
    public void testIsEmpty() {
        String path = "$.store.book[0].author";
        String expectedAuthor = "Nigel Rees";

        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        when(response.path(path)).thenReturn(expectedAuthor, null);

        boolean empty1 = restAssuredResponse.isEmpty(path);
        boolean empty2 = restAssuredResponse.isEmpty(path);

        assertThat("Author should not be empty.", empty1, is(false));
        assertThat("Author should be empty.", empty2, is(true));
    }

    @Test
    public void testGetStatusCode() {
        int expectedStatusCode = 200;
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        when(response.getStatusCode()).thenReturn(expectedStatusCode);

        int statusCode = restAssuredResponse.getStatusCode();

        assertThat("Status code was not matched.", statusCode, is(expectedStatusCode));
    }

    @Test
    public void testGetContentBody() {
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);

        String contentBody = restAssuredResponse.getContentBody();

        assertThat("ContentBody was not matched.", contentBody, is(bookStoreJson));
    }

    @Test
    public void testBodyMatches() {
        RestAssuredResponse restAssuredResponse = new RestAssuredResponse(response);
        String var1 = "var1";
        Matcher var2 = null;
        Object var3 = null;

        restAssuredResponse.bodyMatches(var1, var2, var3);

        verify(response).then();
        verify(validatableResponse).body(var1, var2, var3);
    }
}
