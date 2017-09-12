package kr.co.redbrush.bdd.test.ws;

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
import static org.mockito.Mockito.*;

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

    private String bookStoreJson;
    private String bookJson;

    @Before
    public void before() throws Exception {
        readBookStoreJsonExample();
        readBookJsonExample();

        when(response.getBody()).thenReturn(responseBody);
        when(response.then()).thenReturn(validatableResponse);
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
    public void testGetStringWithRestAssured() {
        testGetStringWithPathType(PathType.RESTASSURED);
    }

    @Test
    public void testGetStringWithJsonPath() {
        testGetStringWithPathType(PathType.JSONPATH);
    }

    private void testGetStringWithPathType(PathType pathType) {
        String path = "$.store.book[0].author";
        String expectedAuthor = "Nigel Rees";

        when(response.path(path)).thenReturn(expectedAuthor);

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse(pathType);
        String author = restAssuredResponse.getString(path);

        LOGGER.debug("Extracted author : {}", author);

        if (pathType == PathType.JSONPATH) {
            verify(response, times(0)).path(path);
        } else {
            verify(response, times(1)).path(path);
        }

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

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse();
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
    public void testGetIntegerWithRestAssured() {
        testGetIntegerWithPathType(PathType.RESTASSURED);
    }

    @Test
    public void testGetIntegerWithJsonPath() {
        testGetIntegerWithPathType(PathType.JSONPATH);
    }

    private void testGetIntegerWithPathType(PathType pathType) {
        String path = "$.expensive";
        Integer expectedExpensive = 10;

        when(response.path(path)).thenReturn(expectedExpensive);

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse(pathType);
        Integer expensive = restAssuredResponse.getInteger(path);

        LOGGER.debug("Extracted expensive : {}", expectedExpensive);

        if (pathType == PathType.JSONPATH) {
            verify(response, times(0)).path(path);
        } else {
            verify(response, times(1)).path(path);
        }

        assertThat("expensive was empty.", expensive, notNullValue());
        assertThat("expensive was not matched.", expensive, is(expectedExpensive));
    }

    @Test
    public void testGetFloatWithRestAssured() {
        testGetFloatWithPathType(PathType.RESTASSURED);
    }

    @Test
    public void testGetFloatWithJsonPath() {
        testGetFloatWithPathType(PathType.JSONPATH);
    }

    private void testGetFloatWithPathType(PathType pathType) {
        String path = "$.store.book[0].price";
        Float expectedPrice = 8.95f;

        when(response.path(path)).thenReturn(expectedPrice);

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse(pathType);
        Float price = restAssuredResponse.getFloat(path);

        LOGGER.debug("Extracted expensive : {}", expectedPrice);

        if (pathType == PathType.JSONPATH) {
            verify(response, times(0)).path(path);
        } else {
            verify(response, times(1)).path(path);
        }

        assertThat("price was empty.", price, notNullValue());
        assertThat("price was not matched.", price, is(expectedPrice));
    }

    @Test
    public void testGetDoubleWithRestAssured() {
        testGetDoubleWithPathType(PathType.RESTASSURED);
    }

    @Test
    public void testGetDoubleWithJsonPath() {
        testGetDoubleWithPathType(PathType.JSONPATH);
    }

    private void testGetDoubleWithPathType(PathType pathType) {
        String path = "$.store.book[0].price";
        Double expectedPrice = 8.95d;

        when(response.path(path)).thenReturn(expectedPrice);

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse(pathType);
        Double price = restAssuredResponse.getDouble(path);

        LOGGER.debug("Extracted expensive : {}", expectedPrice);

        if (pathType == PathType.JSONPATH) {
            verify(response, times(0)).path(path);
        } else {
            verify(response, times(1)).path(path);
        }

        assertThat("price was empty.", price, notNullValue());
        assertThat("price was not matched.", price, is(expectedPrice));
    }

    @Test
    public void testGetObjectWithRestAssured() {
        testGetObjectWithPathType(PathType.RESTASSURED);
    }

    @Test
    public void testGetObjectWithJsonPath() {
        testGetObjectWithPathType(PathType.JSONPATH);
    }

    private void testGetObjectWithPathType(PathType pathType) {
        String path = "$.store.book[*].author";
        List<String> expectedAuthors = Arrays.asList("Nigel Rees", "Evelyn Waugh", "Herman Melville", "J. R. R. Tolkien");

        when(response.path(path)).thenReturn(expectedAuthors);

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse(pathType);
        List<String> authors = restAssuredResponse.getObject(path);

        LOGGER.debug("Extracted authors : {}", authors);

        if (pathType == PathType.JSONPATH) {
            verify(response, times(0)).path(path);
        } else {
            verify(response, times(1)).path(path);
        }

        assertThat("Authors was empty.", authors, notNullValue());
        assertThat("Authors was not matched.", authors, containsInAnyOrder(expectedAuthors.toArray()));
    }

    @Test
    public void testGetObjectSpecificTypeWithRestAssured() {
        testGetObjectSpecificTypeWithPathType(PathType.RESTASSURED);
    }

    @Test
    public void testGetObjectSpecificTypeWithJsonPath() {
        testGetObjectSpecificTypeWithPathType(PathType.JSONPATH);
    }

    private void testGetObjectSpecificTypeWithPathType(PathType pathType) {
        Book expectedBook = createBook();

        if (pathType == PathType.JSONPATH) {
            reset(responseBody);
            when(responseBody.asString()).thenReturn(bookJson);
        } else {
            when(response.as(Book.class)).thenReturn(expectedBook);
        }

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse(pathType);
        Book book = restAssuredResponse.getObject(Book.class);

        LOGGER.debug("Extracted book : {}", book);

        if (pathType == PathType.JSONPATH) {
            verify(response, times(0)).as(Book.class);
        } else {
            verify(response, times(1)).as(Book.class);
        }

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

        RestAssuredResponse restAssuredResponse = createRestAssuredResponse();

        when(response.path(path)).thenReturn(expectedAuthor, null);

        boolean empty1 = restAssuredResponse.isEmpty(path);
        boolean empty2 = restAssuredResponse.isEmpty(path);

        assertThat("Author should not be empty.", empty1, is(false));
        assertThat("Author should be empty.", empty2, is(true));
    }

    @Test
    public void testGetStatusCode() {
        int expectedStatusCode = 200;
        RestAssuredResponse restAssuredResponse = createRestAssuredResponse();

        when(response.getStatusCode()).thenReturn(expectedStatusCode);

        int statusCode = restAssuredResponse.getStatusCode();

        assertThat("Status code was not matched.", statusCode, is(expectedStatusCode));
    }

    @Test
    public void testGetContentBody() {
        RestAssuredResponse restAssuredResponse = createRestAssuredResponse();

        String contentBody = restAssuredResponse.getContentBody();

        assertThat("ContentBody was not matched.", contentBody, is(bookStoreJson));
    }

    @Test
    public void testBodyMatches() {
        RestAssuredResponse restAssuredResponse = createRestAssuredResponse();
        String var1 = "var1";
        Matcher var2 = null;
        Object var3 = null;

        restAssuredResponse.bodyMatches(var1, var2, var3);

        verify(response).then();
        verify(validatableResponse).body(var1, var2, var3);
    }

    private RestAssuredResponse createRestAssuredResponse() {
        return createRestAssuredResponse(PathType.RESTASSURED);
    }

    private RestAssuredResponse createRestAssuredResponse(PathType pathType) {
        RestAssuredResponse restAssuredResponse = null;

        switch (pathType) {
            case RESTASSURED:
                restAssuredResponse = new RestAssuredResponse(response);
                break;
            case JSONPATH:
                restAssuredResponse = new RestAssuredResponse(response, pathType);
                break;
        }
        return restAssuredResponse;
    }
}
