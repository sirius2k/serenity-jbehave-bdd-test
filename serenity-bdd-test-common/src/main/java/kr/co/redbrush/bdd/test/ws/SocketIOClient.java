package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SocketIOClient {
    @Getter
    private Socket socket;

    public SocketIOClient(Socket socket) {
        this.socket = socket;

        bindDefaultEmitterListeners();
        bindCustomEmitterListeners();
    }

    private void bindDefaultEmitterListeners() {
        try {
            this.socket.on(Socket.EVENT_CONNECT, objects -> {
                LOGGER.debug("SocketIO connect");
            });

            this.socket.on(Socket.EVENT_RECONNECTING, objects -> {
                LOGGER.debug("SocketIO reconnecting : {}", objects);
            });

            this.socket.on(Socket.EVENT_DISCONNECT, objects -> {
                LOGGER.debug("SocketIO disconnect : {}", objects);
            });

            this.socket.on(Socket.EVENT_RECONNECT_FAILED, objects -> {
                LOGGER.debug("SocketIO reconnect failed : This message is for PUSH error trap.");
            });
        } catch (Exception e) {
            LOGGER.error("Binding default emitter listeners failed!", e);
        }
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
}