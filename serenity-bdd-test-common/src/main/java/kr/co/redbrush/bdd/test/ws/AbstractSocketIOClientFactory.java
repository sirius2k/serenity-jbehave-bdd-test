package kr.co.redbrush.bdd.test.ws;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;

@Slf4j
public abstract class AbstractSocketIOClientFactory implements SocketIOClientFactory {
    @Value("${socket.io.server.host}")
    protected String serverHost;

    @Value("${socket.io.forceNew:true}")
    protected boolean forceNew;

    @Value("${socket.io.reconnection:true}")
    protected boolean reconnection;

    @Value("${socket.io.recoonectionDelay:2000}")
    protected long reconnectionDelay;

    @Value("${socket.io.reconnectionAttempts:5}")
    protected int reconnectionAttempts;

    @Value("${socket.io.transports:websocket}")
    protected String[] transports;

    @Value("${socket.io.timeout:6000}")
    protected long timeout;

    protected IO.Options options;

    @PostConstruct
    protected void init() {
        options = new IO.Options();
        options.forceNew = forceNew;
        options.reconnection = reconnection;
        options.reconnectionDelay = reconnectionDelay;
        options.reconnectionAttempts = reconnectionAttempts;
        options.transports = transports;
        options.timeout = timeout;

        LOGGER.info("SocketIO server : {}", serverHost);
    }

    @Override
    public SocketIOClient createInstance() throws URISyntaxException {
        return createInstance(serverHost, options);
    }

    @Override
    public SocketIOClient createInstance(IO.Options options) throws URISyntaxException {
        return createInstance(serverHost, options);
    }

    @Override
    public SocketIOClient createInstance(String serverHost, IO.Options options) throws URISyntaxException {
        Socket socket = IO.socket(serverHost, options);
        socket.connect();

        SocketIOClient socketIOClient = createSocketIOClient(socket);

        return socketIOClient;
    }

    public abstract SocketIOClient createSocketIOClient(Socket socket);
}