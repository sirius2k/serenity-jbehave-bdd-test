package kr.co.redbrush.bdd.test.ws;

import kr.co.redbrush.bdd.test.report.serenity.SerenityReport;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by kwpark on 19/03/2017.
 */
@Component
@Slf4j
public class SocketIODriver {
    public static final String SOCKETIO_EVENT_TITLE = "SocketIO Event : ";
    public static final String SOCKETIO_GET_MESSAGE_TITLE = "SocketIO Message Received";

    @Autowired
    private SerenityReport serenityReport;

    @Value("${socket.io.message.timeout:30000}")
    private long timeout;

    public WebServiceResponse getJsonMessage(SocketIOClient socketIOClient) {
        JSONObject json = socketIOClient.waitJsonMessage(timeout);

        LOGGER.info("getJsonMessage : {}", json.toString());

        Serenity.recordReportData()
                .withTitle(SOCKETIO_GET_MESSAGE_TITLE)
                .andContents(json.toString());

        return new SocketIOResponse(json);
    }

    public void sendMessage(String event, String message, SocketIOClient socketIOClient) {
        LOGGER.info("sendMessage. event : {}, message : {}", event, message);

        serenityReport.log(SOCKETIO_EVENT_TITLE + event, message);

        socketIOClient.emit(event, message);
    }

    public void sendMessage(String event, JSONObject json, SocketIOClient socketIOClient) {
        LOGGER.info("sendMessage. event : {}, message : {}", event, json.toString());

        serenityReport.log(SOCKETIO_EVENT_TITLE + event, json);

        socketIOClient.emit(event, json);
    }
}
