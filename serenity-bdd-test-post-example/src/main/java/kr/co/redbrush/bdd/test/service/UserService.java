package kr.co.redbrush.bdd.test.service;

import kr.co.redbrush.bdd.test.ws.SocketIOClient;
import kr.co.redbrush.bdd.test.ws.SocketIOClientContainer;
import kr.co.redbrush.bdd.test.ws.SocketIOClientFactory;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

/**
 * Created by kwpark on 13/03/2017.
 */
@Service
@Slf4j
public class UserService extends BaseService {
    @Autowired
    private SocketIOClientFactory socketIOClientFactory;

    public WebServiceResponse login(String userId, String password) {
        // TODO : implement user authentication request here
        // Below code is example of the authentication
        /*
        LoginRequest loginRequest = LoginRequest.builder()
                .userId(userId)
                .password(password)
                .build();

        LOGGER.debug("login request : {}", loginRequest);

        WebServiceRequest request = webServiceRequestBuilderFactory.createInstance()
                .path(TestURL.Post.CREATE_POST)
                .content(loginRequest)
                .build();

        return restAssuredDriver.post(request);
        */
        WebServiceResponse response = new WebServiceResponse() {
            @Override
            public String getString(String path) {
                return "1";
            }

            @Override
            public String getDefaultString(String path, String defaultString) {
                return "1";
            }

            @Override
            public Integer getInteger(String path) {
                return 1;
            }

            @Override
            public Long getLong(String path) {
                return 1L;
            }

            @Override
            public Float getFloat(String path) {
                return 1.0f;
            }

            @Override
            public Double getDouble(String path) {
                return 1.0d;
            }

            @Override
            public boolean isEmpty(String path) {
                return false;
            }

            @Override
            public int getStatusCode() {
                return 200;
            }

            @Override
            public String getContentBody() {
                return null;
            }

            @Override
            public <T> T getObject(String path) {
                return null;
            }

            @Override
            public <T> T getObject(Class<T> clazz) {
                return null;
            }

            @Override
            public <T> T getObject(String path, Class<T> clazz) {
                return null;
            }

            @Override
            public <T> T getObjectByJsonPath(String path) {
                return null;
            }

            @Override
            public <T> T getObjectByJsonPath(String path, Class<T> clazz) {
                return null;
            }

            @Override
            public void bodyMatches(String var1, Matcher<?> var2, Object... var3) {

            }

            @Override
            public Long getResponseTime() {
                return 0L;
            }
        };

        return response;
    }

    public void connectWebSocket(SocketIOClientContainer container) throws URISyntaxException {
        SocketIOClient socketIOClient =  socketIOClientFactory.createInstance();

        socketIOClient.connect();

        container.setSocketIOClient(socketIOClient);
    }
}
