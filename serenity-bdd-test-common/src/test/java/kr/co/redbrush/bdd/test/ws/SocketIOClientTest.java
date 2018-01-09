package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import kr.co.redbrush.bdd.test.ws.helper.SocketIOServerSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Created by kwpark on 18/04/2017.
 */
@Slf4j
public class SocketIOClientTest extends SocketIOServerSupport {
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
    }

    @After
    public void after() throws Exception {
        socketIOClient.disconnect();
    }

    @Test(timeout = TIMEOUT)
    public void testConnectionToLocalhost() throws Exception {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        String message = "echo";

        socket = client();

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("echo", message);
                socket.on("echoBack", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        values.offer(args[0].toString());
                    }
                });
            }
        });

        socket.connect();

        assertThat("Unexpected response.", values.take(), is(message));

        socket.close();
    }

    @Test(timeout = TIMEOUT)
    public void testConnectAndDisconnect() throws Exception {
        Socket expectedSocket = socketIOClient.getSocket();

        assertThat("Socket is not valid.", expectedSocket, is(socket));

        socketIOClient.connect();

        assertThat("Socket is not connected.", socketIOClient.connected(), is(true));

        socketIOClient.disconnect();

        assertThat("Socket is connected.", socketIOClient.connected(), is(false));
    }

    @Test(timeout = TIMEOUT)
    public void testConnectWhenAlreadyConnected() throws Exception {
        StopWatch stopWatch = new StopWatch();
        Socket expectedSocket = socketIOClient.getSocket();

        assertThat("Socket is not valid.", expectedSocket, is(socket));

        stopWatch.start();
        socketIOClient.connect();
        assertThat("Socket is not connected.", socketIOClient.connected(), is(true));
        assertThat("Unexpected connection time", stopWatch.getTime(), greaterThanOrEqualTo(SocketIOClient.SOCKETIO_CHECK_INTERVAL));
        stopWatch.stop();

        stopWatch.reset();

        stopWatch.start();
        assertThat("Socket is not connected.", socketIOClient.connected(), is(true));
        socketIOClient.connect();
        assertThat("Unexpected connection time", stopWatch.getTime(), lessThan(SocketIOClient.SOCKETIO_CHECK_INTERVAL));
        stopWatch.stop();

        socketIOClient.disconnect();

        assertThat("Socket is connected.", socketIOClient.connected(), is(false));
    }

    @Test(timeout = TIMEOUT)
    public void testBindEventAndEmit() throws Exception {
        String echoBackEvent = "echoBack";
        String echoMessage = "Echo message";
        DefaultEmitterListener listener = new DefaultEmitterListener(socketIOClient);

        socketIOClient.bindEvent(echoBackEvent, listener);

        socketIOClient.connect();
        socketIOClient.emit("echo", echoMessage);
        socketIOClient.waitMessage();
        socketIOClient.disconnect();

        assertThat("Unexpected result.", socketIOClient.getMessages().take(), is(echoMessage));
    }

    @Test(timeout = TIMEOUT)
    public void testBindEventAndWaitTestMessage() throws Exception {
        String echoBackEvent = "echoBack";
        String echoMessage = "Echo message";
        String actualMessage = null;
        DefaultEmitterListener listener = new DefaultEmitterListener(socketIOClient);

        socketIOClient.bindEvent(echoBackEvent, listener);

        socketIOClient.connect();
        socketIOClient.emit("echo", echoMessage);
        actualMessage = socketIOClient.waitTextMessage();
        socketIOClient.disconnect();

        assertThat("Unexpected result.", actualMessage, is(echoMessage));
    }

    @Test(timeout = TIMEOUT)
    public void testEmit() throws Exception {
        String getBookStoreEvent = "getBookStore";
        JSONObject actualResponse = null;
        DefaultEmitterListener listener = new DefaultEmitterListener(socketIOClient);

        socketIOClient.bindEvent(getBookStoreEvent, listener);

        socketIOClient.connect();
        socketIOClient.emit("getBookStore");
        actualResponse = socketIOClient.waitJsonMessage();
        socketIOClient.disconnect();

        LOGGER.debug("Actual Response : {}, length : {}", actualResponse, actualResponse.length());

        assertThat("Unexpected result.", actualResponse, notNullValue());
        assertThat("Unexpected result.", actualResponse.length(), is(2));
    }

    @Test(timeout = TIMEOUT)
    public void testEmitJson() throws Exception {
        DefaultEmitterListener listener = new DefaultEmitterListener(socketIOClient);

        JSONObject json = new JSONObject();
        json.put("id", 1);

        JSONObject actualResponse = null;

        socketIOClient.bindEvent("getPost", listener);

        socketIOClient.connect();
        socketIOClient.emit("getPost", json);
        actualResponse = socketIOClient.waitJsonMessage();
        socketIOClient.disconnect();

        assertThat("Unexpected result.", actualResponse.get("id"), is(1));
        assertThat("Unexpected result.", actualResponse.get("title"), is("title"));
        assertThat("Unexpected result.", actualResponse.get("body"), is("test body"));
        assertThat("Unexpected result.", actualResponse.get("userId"), is(1));
    }

    @Test(timeout = TIMEOUT)
    public void testEmitJsonAndProcessAck() throws Exception {
        DefaultEmitterListener listener = new DefaultEmitterListener(socketIOClient);
        boolean ackCalled = false;

        JSONObject json = new JSONObject();
        json.put("id", 1);

        JSONObject actualResponse = null;

        socketIOClient.bindEvent("getPost", listener);

        socketIOClient.connect();
        socketIOClient.emit("getPost", json);
        actualResponse = socketIOClient.waitJsonMessage();
        socketIOClient.disconnect();

        assertThat("Unexpected result.", actualResponse.get("id"), is(1));
        assertThat("Unexpected result.", actualResponse.get("title"), is("title"));
        assertThat("Unexpected result.", actualResponse.get("body"), is("test body"));
        assertThat("Unexpected result.", actualResponse.get("userId"), is(1));
    }
}
