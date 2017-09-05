package kr.co.redbrush.bdd.test.ws;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * Created by kwpark on 19/03/2017.
 */
@Builder
@Data
public class WebServiceRequest {
    private String serverHost;
    private String path;
    private String url;
    private @Singular Map<String, Object> pathParameters;
    private @Singular Map<String, Object> headers;
    private @Singular Map<String, Object> queryParameters;
    private String contentType;
    private Object content;
    private boolean urlEncodingEnabled;
    private HttpMethod httpMethod;

    public String getUrl() {
        return serverHost + path;
    }
}
