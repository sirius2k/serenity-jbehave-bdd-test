package kr.co.redbrush.bdd.test.ws;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.reports.AndContent;
import net.serenitybdd.core.reports.WithTitle;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@Slf4j
@RunWith(PowerMockRunner.class)
@PrepareForTest(Serenity.class)
public class SocketIODriverTest {
    @InjectMocks
    private SocketIODriver socketIODriver;

    @Mock
    private SocketIOClient socketIOClient;

    @Mock
    private JSONObject json;

    @Mock
    private WithTitle withTitle;

    @Mock
    private AndContent andContent;

    private String jsonBody = "{}";

    private long timeout = 30000L;

    @Before
    public void before() {
        ReflectionTestUtils.setField(socketIODriver, "timeout", timeout);

        when(json.toString()).thenReturn(jsonBody);

        mockStatic(Serenity.class);

        when(Serenity.recordReportData()).thenReturn(withTitle);
        when(withTitle.withTitle(any(String.class))).thenReturn(andContent);
    }

    @Test
    public void testGetJsonResponse() {
        when(socketIOClient.waitJsonMessage(timeout)).thenReturn(json);

        WebServiceResponse response = socketIODriver.getJsonMessage(socketIOClient);

        assertThat("Unexpected response.", response, notNullValue());
        assertThat("Unexpected statusCode", response instanceof SocketIOResponse, is(true));
        assertThat("Unexpected contentBody", response.getContentBody(), is(jsonBody));
        verify(andContent).andContents(any(String.class));
    }

    @Test
    public void testSendMessage() {
        String event = "event";
        String message = "message";

        socketIODriver.sendMessage(event, message, socketIOClient);

        verify(socketIOClient).emit(event, message);
        verify(andContent).andContents(any(String.class));
    }

    @Test
    public void testSendJsonMessage() {
        String event = "event";
        JSONObject json = new JSONObject();

        socketIODriver.sendMessage(event, json, socketIOClient);

        verify(socketIOClient).emit(event, json);
        verify(andContent).andContents(any(String.class));
    }
}
