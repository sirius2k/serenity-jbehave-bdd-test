package kr.co.redbrush.bdd.test.service;

import kr.co.redbrush.bdd.test.common.TestURL;
import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.ws.WebServiceRequest;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by kwpark on 13/03/2017.
 */
@Service
@Slf4j
public class PostService extends BaseService {

    public WebServiceResponse createPost(Post post) {
        WebServiceRequest request = webServiceRequestBuilderFactory.createInstance()
                .path(TestURL.Post.CREATE_POST)
                .content(post)
                .build();

        return restAssuredDriver.post(request);

    }

    public WebServiceResponse getPost(Integer id) {
        WebServiceRequest request = webServiceRequestBuilderFactory.createInstance()
                .path(TestURL.Post.GET_POST)
                .pathParameter("id", id)
                .build();

        return restAssuredDriver.get(request);
    }
}
