package kr.co.redbrush.bdd.test.context;

import kr.co.redbrush.bdd.test.domain.Post;
import kr.co.redbrush.bdd.test.ws.WebServiceResponse;
import lombok.Data;

/**
 * Created by kwpark on 20/03/2017.
 */

@Data
public class TestContext {
    private Post post;
    private WebServiceResponse lastResponse;
}
