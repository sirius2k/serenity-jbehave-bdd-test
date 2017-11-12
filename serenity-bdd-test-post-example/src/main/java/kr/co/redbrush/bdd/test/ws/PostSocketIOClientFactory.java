package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import org.springframework.stereotype.Component;

@Component
public class PostSocketIOClientFactory extends AbstractSocketIOClientFactory {

    @Override
    public SocketIOClient createSocketIOClient(Socket socket) {
        return new PostSocketIOClient(socket);
    }
}
