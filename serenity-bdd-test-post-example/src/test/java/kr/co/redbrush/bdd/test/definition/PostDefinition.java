package kr.co.redbrush.bdd.test.definition;

import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.steps.PostSteps;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

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

    @BeforeScenario
    public void before() {
    }

    @AfterScenario
    public void after() {
        testContextService.clearContext();

        LOGGER.info("After scenario called. TestContext is cleared.");
    }

    @Given("title '$title', body '$body' and userId '$userId'")
    public void givenTitleBodyUserId(String title, String body, Integer userId) {
        Post post = Post.builder()
                .title(title)
                .body(body)
                .userId(userId)
                .build();

        LOGGER.debug("given post : {}", post);

        testContextService.getTestContext().put("post", post);
    }

    @Given("User creates post")
    @When("User creates post")
    public void whenUserCreatePostWith() {
        Post post = testContextService.getTestContext().get("post");

        postSteps.createPost(post.getTitle(), post.getBody(), post.getUserId());
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

    @Given("a post id '$id'")
    public void giveAPostId(Integer id) {
        testContextService.getTestContext().put("postId", id);
    }

    @When("User request a post")
    public void whenUserReuqestAPost() {
        Integer id = testContextService.getTestContext().get("postId");

        postSteps.getPost(id);
    }

    @When("User request a post with id '$id'")
    public void whenUserReuqestAPostWith(Integer id) {
        postSteps.getPost(id);
    }

    @Then("server should return Post containing id '$id', not empty title, not empty body and userId '$userId'")
    public void thenRequestingAPostReturns(Integer id, Integer userId) {
        WebServiceResponse response = testContextService.getLastResponse();

        LOGGER.debug("Response : {}", response.getContentBody());

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
