package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class SocketIOClient {
    private Socket socket;

    public SocketIOClient(Socket socket) {
        this.socket = socket;

        init();
        addEmitterListener();
        socket.connect();
    }

    public void init() {
        try {
            this.socket.on(Socket.EVENT_CONNECT, objects -> {
                LOGGER.debug("IOSocket connect");
            });

            this.socket.on(Socket.EVENT_RECONNECTING, objects -> {
                LOGGER.debug("IOSocket reconnecting : {}", objects);
            });

            this.socket.on(Socket.EVENT_DISCONNECT, objects -> {
                LOGGER.debug("IOSocket disconnect : {}", objects);
            });

            this.socket.on(Socket.EVENT_RECONNECT_FAILED, objects -> {
                LOGGER.debug("IOSocket reconnect failed : This message is for PUSH error trap.");
            });

            this.socket.on("msg", message -> {
                LOGGER.debug("IOSocket message {}", message);
            });
        } catch (Exception e) {
            LOGGER.error("Socket.IO initialization failed!", e);
        }
    }

    public abstract void addEmitterListener();

    public void close() {
        if (socket!=null) {
            socket.close();
        }
    }
}