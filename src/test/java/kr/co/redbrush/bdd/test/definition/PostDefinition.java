package kr.co.redbrush.bdd.test.definition;

import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.steps.PostSteps;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.fail;

/**
 * Created by kwpark on 13/03/2017.
 */
@Slf4j
public class PostDefinition extends BaseTestDefinition {
    @Autowired
    private PostSteps postSteps;

    @PostConstruct
    public void init() {
    }

    @AfterScenario
    public void afterScenario() {
        testContextService.clearContext();

        LOGGER.info("After scenario called. TestContext is cleared.");
    }

    @When("User create post with title '$title', body '$body' and userId '$userId'")
    public void whenUserCreatePostWith(String title, String body, Integer userId) {
        postSteps.createPost(title, body, userId);
    }

    @Then("Created Post contains id '$id', title '$title', body '$body' and userId '$userId'")
    @Alias("A returned Post contains id '$id', title '$title', body '$body' and userId '$userId'")
    public void thenCreatedPostContains(Integer id, String title, String body, Integer userId) {
        WebServiceResponse response = testContextService.getLastResponse();

        Post post = response.getObject(Post.class);

        assertThat(post.getId(), equalTo(id));
        assertThat(post.getTitle(), equalTo(title));
        assertThat(post.getBody(), equalTo(body));
        assertThat(post.getUserId(), equalTo(userId));
    }

    @When("User request a post with id '$id'")
    public void whenUserReuqestAPostWith(Integer id) {
        postSteps.getPost(id);
    }

    @Then("A returned Post contains id '$id', not empty title, not empty body and userId '$userId'")
    public void thenRequestingAPostReturns(Integer id, Integer userId) {
        WebServiceResponse response = testContextService.getLastResponse();

        Post post = response.getObject(Post.class);

        assertThat(post.getId(), equalTo(id));
        assertThat(post.getTitle(), not(isEmptyString()));
        assertThat(post.getBody(), not(isEmptyString()));
        assertThat(post.getUserId(), equalTo(userId));


    }

    @When("User create post with with title '$title', body '$body' and userId '$userId' and request a post with id '$id'")
    public void whenUserCreatePostAndRequestAPostWith(String title, String body, Integer userId, Integer id) {
        postSteps.createPost(title, body, userId);
        postSteps.getPost(id);
    }
}
