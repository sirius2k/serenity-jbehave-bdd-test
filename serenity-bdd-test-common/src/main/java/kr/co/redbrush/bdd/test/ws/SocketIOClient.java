package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@Slf4j
public abstract class SocketIOClient {
    @Getter
    protected Socket socket;

    public SocketIOClient(Socket socket) {
        this.socket = socket;

        bindDefaultEmitterListeners();
        bindCustomEmitterListeners();
    }

    private void bindDefaultEmitterListeners() {
        try {
            onConnect(objects -> {
                LOGGER.debug("SocketIO connected. : {}", objects);
            });

            onDisConnect(objects -> {
                LOGGER.debug("SocketIO disconnect : {}", objects);
            });

            onReconnecting(objects -> {
                LOGGER.debug("SocketIO reconnecting : {}", objects);
            });

            onReconnectFailed(objects -> {
                LOGGER.debug("SocketIO reconnect failed : This message is for PUSH error trap.");
            });
        } catch (Exception e) {
            LOGGER.error("Binding default emitter listeners failed!", e);
        }
    }

    protected void onConnect(Emitter.Listener listener) {
        socket.on(Socket.EVENT_CONNECT, listener);
    }

    protected void onDisConnect(Emitter.Listener listener) {
        socket.on(Socket.EVENT_DISCONNECT, listener);
    }

    protected void onReconnecting(Emitter.Listener listener) {
        socket.on(Socket.EVENT_RECONNECTING, listener);
    }

    protected void onReconnectFailed(Emitter.Listener listener) {
        socket.on(Socket.EVENT_RECONNECT_FAILED, listener);
    }

    public void connect() {
        if (!socket.connected()) {
            socket.connect();
        }
    }

    public void bindEmitterListener(String event, Emitter.Listener listener) {
        socket.on(event, listener);
    }

    public abstract void bindCustomEmitterListeners();

    public boolean connected() {
        return socket.connected();
    }

    public void close() {
        if (socket!=null && socket.connected()) {
            socket.close();
        }
    }

    public void waitFor(long timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted.", e);
        }
    }
}