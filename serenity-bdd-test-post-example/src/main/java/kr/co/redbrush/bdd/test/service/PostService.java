package kr.co.redbrush.bdd.test.service;

import kr.co.redbrush.bdd.test.common.SocketIOEvent;
import kr.co.redbrush.bdd.test.common.TestURL;
import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.ws.SocketIOClient;
import kr.co.redbrush.bdd.test.ws.SocketIOClientContainer;
import kr.co.redbrush.bdd.test.ws.WebServiceRequest;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Created by kwpark on 13/03/2017.
 */
@Service
@Slf4j
public class PostService extends BaseService {

    public WebServiceResponse createPost(Post post) {
        WebServiceRequest request = webServiceRequestBuilderFactory.createInstance()
                .path(TestURL.Post.CREATE_POST)
                .content(post)
                .build();

        return restAssuredDriver.post(request);

    }

    public WebServiceResponse getPost(Integer id) {
        WebServiceRequest request = webServiceRequestBuilderFactory.createInstance()
                .path(TestURL.Post.GET_POST)
                .pathParameter("id", id)
                .build();

        return restAssuredDriver.get(request);
    }

    public void requestPostFromWebsocket(SocketIOClientContainer container, Integer id) throws JSONException {
        SocketIOClient client = container.getSocketIOClient();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);

        socketIODriver.sendMessage(SocketIOEvent.Post.GET_POST, jsonObject, client);
    }

    public WebServiceResponse getPostFromWebSocket(SocketIOClientContainer container) throws JSONException {
        return socketIODriver.getJsonMessage(container.getSocketIOClient());
    }

    public void sendTextMessage(SocketIOClientContainer container, String event, String message) {
        SocketIOClient client = container.getSocketIOClient();

        socketIODriver.sendMessage(event, message, client);
    }
}
