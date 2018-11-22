package kr.co.redbrush.bdd.test.definition;

import kr.co.redbrush.bdd.test.domain.User;
import kr.co.redbrush.bdd.test.steps.UserSteps;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by kwpark on 13/03/2017.
 */
@Slf4j
public class UserDefinition extends BaseTestDefinition {
    @Steps
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
