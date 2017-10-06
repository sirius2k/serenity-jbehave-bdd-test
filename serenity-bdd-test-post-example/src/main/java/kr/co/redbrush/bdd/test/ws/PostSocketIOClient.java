package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostSocketIOClient extends SocketIOClient {

    public PostSocketIOClient(Socket socket) {
        super(socket);
    }

    @Override
    public void bindCustomEmitterListeners() {
        LOGGER.debug("bind custom emitter listeners");
    }
}
