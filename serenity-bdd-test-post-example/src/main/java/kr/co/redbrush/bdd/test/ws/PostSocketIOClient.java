package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class PostSocketIOClient extends SocketIOClient {
    public PostSocketIOClient(Socket socket) {
        super(socket);
    }

    @Override
    public void bindCustomEmitterListeners() {
        socket.on(PostSocketIOClientEvent.EVENT_GET_POST, objects -> {
            JSONObject json = (JSONObject)objects[0];

            LOGGER.debug("getPost : {}", json);
        });
    }
}
