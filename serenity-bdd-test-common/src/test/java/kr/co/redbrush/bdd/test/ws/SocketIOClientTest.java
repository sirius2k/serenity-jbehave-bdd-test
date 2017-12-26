package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class SocketIOClientTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public Socket socket;

    private SocketIOClient socketIOClient;
    private Emitter.Listener listener;
    private String testEvent = "test";

    @Before
    public void before() throws Exception {
        listener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LOGGER.debug("call");
            }
        };

        socketIOClient = new SocketIOClient(socket) {
            @Override
            public void bindCustomEmitterListeners() {
                socket.on(testEvent, listener);
            }
        };
    }

    @Test
    public void testCreateInstance() throws Exception {
        assertThat("Socket is not valid.", socketIOClient.getSocket(), is(socket));
        verify(socket).on(eq(Socket.EVENT_CONNECT), any(Emitter.Listener.class));
        verify(socket).on(eq(Socket.EVENT_RECONNECTING), any(Emitter.Listener.class));
        verify(socket).on(eq(Socket.EVENT_DISCONNECT), any(Emitter.Listener.class));
        verify(socket).on(eq(Socket.EVENT_RECONNECT_FAILED), any(Emitter.Listener.class));
        verify(socket).on(eq(testEvent), eq(listener));
    }

    @Test
    public void testConnect() throws Exception {
        when(socket.connected()).thenReturn(false, true);

        socketIOClient.connect();
        socketIOClient.connect();
        verify(socket, times(1)).connect();
    }

    @Test
    public void testAddEmitterListener() throws Exception {
        String event = "event";

        socketIOClient.bindEmitterListener(event, listener);

        verify(socket).on(event, listener);
    }

    @Test
    public void testConnected() throws Exception {
        when(socket.connected()).thenReturn(false, true);

        assertThat("Socket was connected.", socketIOClient.connected(), is(false));
        assertThat("Socket was not connected.", socketIOClient.connected(), is(true));
    }

    @Test
    public void testClose() throws Exception {
        when(socket.connected()).thenReturn(true, false);

        socketIOClient.close();
        socketIOClient.close();
        verify(socket, times(1)).close();
    }
}
