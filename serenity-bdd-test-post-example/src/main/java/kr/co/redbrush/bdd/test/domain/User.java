package kr.co.redbrush.bdd.test.domain;

import kr.co.redbrush.bdd.test.ws.SocketIOClient;
import kr.co.redbrush.bdd.test.ws.SocketIOClientContainer;
import lombok.Data;

@Data
public class User implements SocketIOClientContainer {
    private SocketIOClient socketIOClient = null;
    private String userId;

    public User() {

    }

    public User(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }
}
