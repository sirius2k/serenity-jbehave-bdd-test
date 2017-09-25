package kr.co.redbrush.bdd.test.ws;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class SocketIOFactoryTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private SocketIOFactory factory;
    private String serverHost = "http://localhost";

    @Before
    public void before() throws Exception {
        factory = new SocketIOFactory();

        ReflectionTestUtils.invokeMethod(factory, "init");
    }

    @Test
    public void testCreateInstance() throws Exception {
        Socket socket = factory.createSocket(serverHost);

        assertThat("Socket is null.", socket, notNullValue());
    }

    @Test
    public void testCreateInstanceWithOptions() throws Exception {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.reconnectionDelay = 3000;
        options.reconnectionAttempts = 10;
        options.transports = new String[]{ WebSocket.NAME};

        Socket socket = factory.createSocket(serverHost, options);

        assertThat("Socket is null.", socket, notNullValue());
    }
}
