package kr.co.redbrush.bdd.test.ws;

import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import static kr.co.redbrush.bdd.test.ws.PostSocketIOClientEvent.EVENT_GET_POST;

@Slf4j
public class PostSocketIOClient extends SocketIOClient {
    public PostSocketIOClient(Socket socket) {
        super(socket);
    }

    @Override
    public void bindCustomEmitterListeners() {
        socket.on(EVENT_GET_POST, objects -> {
            JSONObject json = (JSONObject)objects[0];

            addMessage(json);

            LOGGER.debug("getPost : {}", json);
        });
    }
}
