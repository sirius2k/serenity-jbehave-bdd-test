package kr.co.redbrush.bdd.test.ws;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.socket.client.Ack;
import kr.co.redbrush.bdd.test.exception.HttpMethodNotSpecifiedException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.rest.SerenityRest.given;

/**
 * Created by kwpark on 19/03/2017.
 */
@Component
@Slf4j
public class SocketIODriver {
    @Value("${socket.io.message.timeout:30000}")
    private long timeout;

    public WebServiceResponse getJsonMessage(SocketIOClient socketIOClient) {
        JSONObject json = socketIOClient.waitJsonMessage(timeout);

        return new SocketIOResponse(json);
    }

    public void sendMessage(String event, String message, SocketIOClient socketIOClient) {
        socketIOClient.emit(event, message);
    }

    public void sendMessage(String event, JSONObject json, SocketIOClient socketIOClient) {
        socketIOClient.emit(event, json);
    }
}
