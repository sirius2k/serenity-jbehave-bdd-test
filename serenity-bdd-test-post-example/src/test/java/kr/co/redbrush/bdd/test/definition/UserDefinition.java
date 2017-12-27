package kr.co.redbrush.bdd.test.definition;

import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.domain.User;
import kr.co.redbrush.bdd.test.domain.request.LoginRequest;
import kr.co.redbrush.bdd.test.steps.PostSteps;
import kr.co.redbrush.bdd.test.steps.UserSteps;
import kr.co.redbrush.bdd.test.ws.PostSocketIOClient;
import kr.co.redbrush.bdd.test.ws.SocketIOClient;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import org.jbehave.core.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import java.net.URISyntaxException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

/**
 * Created by kwpark on 13/03/2017.
 */
@Slf4j
public class UserDefinition extends BaseTestDefinition {
    @Autowired
    private UserSteps userSteps;

    @PostConstruct
    public void init() {
    }

    @Given("$userWithIndex logged in")
    public void givenUserLoggedInWith(String userWithIndex) throws URISyntaxException {
        String userId = "user1";
        String password = "password";

        WebServiceResponse response = userSteps.login(userId, password);

        User user = new User();
        user.setUserId(response.getString("userId"));

        userSteps.connectWebsocket(user);

        assertThat("SocketIO was not connected.", user.getSocketIOClient().getSocket().connected(), is(true));
        assertThat("userId should not be null : {}", user.getUserId(), notNullValue());
        assertThat("socket should not be null : {}", user.getSocketIOClient(), notNullValue());

        testContextService.getTestContext().put(userWithIndex, user);
    }
}
