package kr.co.redbrush.bdd.test.steps;

import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.domain.User;
import kr.co.redbrush.bdd.test.service.PostService;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.Step;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by kwpark on 13/03/2017.
 */

@Component
@Data
@EqualsAndHashCode(callSuper=false)
@Slf4j
public class PostSteps extends BaseSteps {
    @Autowired
    private PostService postService;

    @Step("Step : create post")
    public WebServiceResponse createPost(String title, String body, Integer userId) {
        Post post = Post.builder()
                .title(title)
                .body(body)
                .userId(userId)
                .build();

        WebServiceResponse response = postService.createPost(post);

        testContextService.setLastResponse(response);

        return response;
    }

    @Step
    public WebServiceResponse getPost(Integer id) {
        WebServiceResponse response = postService.getPost(id);

        testContextService.setLastResponse(response);

        return response;
    }

    @Step
    public WebServiceResponse getPostFromWebSocket(String userWithIndex, Integer id) throws JSONException {
        User user = testContextService.getUser(userWithIndex);
        WebServiceResponse response = postService.getPostFromWebSocket(user, id);

        testContextService.setLastResponse(response);

        return response;
    }
}
