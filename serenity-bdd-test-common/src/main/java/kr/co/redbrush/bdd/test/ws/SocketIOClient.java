package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class SocketIOClient {
    public static final long SOCKETIO_CHECK_INTERVAL = 100L;

    @Getter @Setter
    private long timeout = 30000L;

    @Getter
    protected final Socket socket;

    @Getter
    protected final BlockingQueue<Object> messages = new LinkedBlockingQueue<>();

    public SocketIOClient(Socket socket) {
        this.socket = socket;

        bindDefaultEmitterListeners();
        bindCustomEmitterListeners();
    }

    private void bindDefaultEmitterListeners() {
        socket.on(Socket.EVENT_CONNECT, objects -> {
            onConnect(objects);
        });

        socket.on(Socket.EVENT_DISCONNECT, objects -> {
            onDisConnect(objects);
        });

        socket.on(Socket.EVENT_RECONNECTING, objects -> {
            onReconnecting(objects);
        });

        socket.on(Socket.EVENT_RECONNECT_FAILED, objects -> {
            onReconnectFailed(objects);
        });
    }

    public void onConnect(Object... args) {
        LOGGER.debug("SocketIO connect : {}", args);
    }

    public void onDisConnect(Object... args) {
        LOGGER.debug("SocketIO disconnect : {}", args);
    }

    public void onReconnecting(Object... args) {
        LOGGER.debug("SocketIO reconnecting : {}", args);
    }

    public void onReconnectFailed(Object... args) {
        LOGGER.debug("SocketIO reconnect failed : This message is for PUSH error trap.");
    }

    public abstract void bindCustomEmitterListeners();

    public void bindEvent(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public void connect() {
        if (!socket.connected()) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            socket.connect();

            while (stopWatch.getTime() < timeout && !socket.connected()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SOCKETIO_CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    LOGGER.warn("Sleep interrupted. {}", e);
                }
            }

            stopWatch.stop();

            LOGGER.info("Socket connected : {}, elapsed : {} ms", socket.connected(), stopWatch.getTime());
        }
    }

    public boolean connected() {
        return socket.connected();
    }

    public void close() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();;

        if (socket!=null && socket.connected()) {
            socket.close();

            while (stopWatch.getTime() < timeout && socket.connected()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(SOCKETIO_CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    LOGGER.warn("Sleep interrupted. {}", e);
                }
            }
        }

        stopWatch.stop();
    }

    public void disconnect() {
        close();
    }

    public void emit(String event) {
        socket.emit(event);
    }

    public void emit(String event, JSONObject json) {
        socket.emit(event, json);
    }

    public void emit(String event, String content) {
        socket.emit(event, content);
    }

    public void waitMessage() {
        waitMessage(timeout);
    }

    public void waitMessage(long timeout) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        while (stopWatch.getTime() < timeout && messages.size()==0) {
            try {
                TimeUnit.MILLISECONDS.sleep(SOCKETIO_CHECK_INTERVAL);
            } catch (InterruptedException e) {
                LOGGER.warn("Sleep interrupted. {}", e);
            }
        }

        LOGGER.info("Message received. elapsed : {} ms", stopWatch.getTime());

        stopWatch.stop();
    }

    public JSONObject waitJsonMessage() {
        return waitJsonMessage(timeout);
    }

    public JSONObject waitJsonMessage(long timeout) {
        waitMessage(timeout);

        return getJsonMessage();
    }

    public String waitTextMessage() {
        return waitTextMessage(timeout);
    }

    public String waitTextMessage(long timeout) {
        waitMessage(timeout);

        return getTextMessage();
    }

    private JSONObject getJsonMessage() {
        JSONObject json = null;

        try {
            json = (JSONObject)messages.take();
        } catch (InterruptedException e) {
            LOGGER.warn("Message taking interrupted. {}", e);
        }

        return json;
    }

    private String getTextMessage() {
        String message = null;

        try {
            Object obj = messages.take();

            if (obj!=null) {
                message = obj.toString();
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Message taking interrupted. {}", e);
        }

        return message;
    }

    public void addMessage(Object message) {
        messages.add(message);
    }
}