package kr.co.redbrush.bdd.test.ws;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class AbstractSocketIOClientFactoryTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private AbstractSocketIOClientFactory factory;
    private String serverHost = "http://localhost";

    @Before
    public void before() throws Exception {
        factory = new AbstractSocketIOClientFactory() {
            @Override
            public SocketIOClient createSocketIOClient(Socket socket) {
                return new SocketIOClient(socket) {
                    @Override
                    public void bindCustomEmitterListeners() {
                        LOGGER.debug("Binding custom emitter listerns");
                    }
                };
            }
        };

        ReflectionTestUtils.setField(factory, "serverHost", "http://www.github.com");
        ReflectionTestUtils.setField(factory, "forceNew", true);
        ReflectionTestUtils.setField(factory, "reconnection", true);
        ReflectionTestUtils.setField(factory, "reconnectionDelay", 2000);
        ReflectionTestUtils.setField(factory, "reconnectionAttempts", 5);
        ReflectionTestUtils.setField(factory, "transports", new String[] { WebSocket.NAME });
        ReflectionTestUtils.invokeMethod(factory, "init");
    }

    @Test
    public void testCreateInstance() throws Exception {
        SocketIOClient socketIOClient = factory.createInstance();

        assertThat("SocketIOClient is null.", socketIOClient, notNullValue());
        assertThat("Socket is null.", socketIOClient.getSocket(), notNullValue());
    }

    @Test
    public void testCreateInstanceWithOptions() throws Exception {
        IO.Options options = new IO.Options();
        SocketIOClient socketIOClient = factory.createInstance(options);

        assertThat("SocketIOClient is null.", socketIOClient, notNullValue());
        assertThat("Socket is null.", socketIOClient.getSocket(), notNullValue());
    }

    @Test
    public void testCreateInstanceWithHostAndOptions() throws Exception {
        IO.Options options = new IO.Options();
        SocketIOClient socketIOClient = factory.createInstance(serverHost, options);

        assertThat("SocketIOClient is null.", socketIOClient, notNullValue());
        assertThat("Socket is null.", socketIOClient.getSocket(), notNullValue());
    }
}
