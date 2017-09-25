package kr.co.redbrush.bdd.test.ws;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;

@Component
@Slf4j
public class SocketIOFactory {
    private IO.Options options;

    @PostConstruct
    private void init() {
        options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.reconnectionDelay = 2000;
        options.reconnectionAttempts = 15;
        options.transports = new String[]{ WebSocket.NAME};
    }

    public Socket createSocket(String serverHost) throws URISyntaxException {
        return IO.socket(serverHost, this.options);
    }

    public Socket createSocket(String serverHost, IO.Options options) throws URISyntaxException {
        return IO.socket(serverHost, options);
    }
}