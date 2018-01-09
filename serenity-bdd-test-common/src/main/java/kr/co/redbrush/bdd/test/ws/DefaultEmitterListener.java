package kr.co.redbrush.bdd.test.ws;

import io.socket.emitter.Emitter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DefaultEmitterListener implements Emitter.Listener {
    private SocketIOClient socketIOClient;

    public DefaultEmitterListener(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    @Override
    public void call(Object... args) {
        LOGGER.debug("Event called. args : {}", args);

        if (args!=null && args.length>0) {
            socketIOClient.addMessage(args[0]);
        }
    }
}
