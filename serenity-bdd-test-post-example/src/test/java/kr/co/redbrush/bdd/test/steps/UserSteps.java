package kr.co.redbrush.bdd.test.steps;

import kr.co.redbrush.bdd.test.domain.User;
import kr.co.redbrush.bdd.test.service.UserService;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;


/**
 * Created by kwpark on 13/03/2017.
 */
@Component
@Data
@EqualsAndHashCode(callSuper=false)
@Slf4j
public class UserSteps extends BaseSteps {
    @Autowired
    private UserService userService;

    @Step
    public WebServiceResponse login(String userId, String password) {

        WebServiceResponse response = userService.login(userId, password);

        testContextService.setLastResponse(response);

        return response;
    }

    @Step
    public void connectWebsocket(User user) throws URISyntaxException {
        userService.connectWebSocket(user);
    }
}
