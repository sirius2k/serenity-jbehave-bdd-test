## Synopsis
This project is base project for BDD Test which is created on top of Serenity, Spring Framework and JBehave. The objective of this project is to create REST API and WebSocket integration test quickly.

## Code Example
Story which describes acceptance criteria
```
When User create post with title '${post.title}', body '${post.body}' and userId '${post.userId}'
Then Created Post contains id '101', title '${post.title}', body '${post.body}' and userId '${post.userId}'

```
Java code which is mapped to steps in acceptance criteria
```java
@When("User create post with title '$title', body '$body' and userId '$userId'")
public void whenUserCreatePostWith(String title, String body, Integer userId) {
    postSteps.createPost(title, body, userId);
}
```

## Test Structure
1. Requirements are consist of features and acceptance criteria(stories).
2. Each feature have set of stories and acceptance criteria.
3. Each story have set of acceptance criteria.
4. Acceptance criteria is a test cases described as test scenario.

You can describe narrative of each requirement from file narrative.txt in feature folder /test/resources/stories/[feature]/
```
[Requirement]
Narrative of requirement

Example)
JSONPlaceHolder post requirements
In order to share and manage posts
As a User
I want to view, create, update and delete a post
```
And, you can also describe the narrative of each story in the story file as follows.
```
Narrative:
Narrative of story

Example)
Narrative:
In order to share and manage posts
As a User
I want to view, create, update and delete a post
```

## What you need to implement before creating tests
Prior to write tests, you will need to implement your own Factory classes and SocketIOClient.

*PostWebServiceRequestBuilderFactory.java*
```
Component
@Slf4j
public class PostWebServiceRequestBuilderFactory extends SpringWebServiceRequestBuilderFactory {

    @PostConstruct
    public void init() {
        LOGGER.debug("<Post Construct> ServerHost : {}, defaultContentType : {}", serverHost, defaultContentType);
    }

    public WebServiceRequest.WebServiceRequestBuilder createInstance() {
        WebServiceRequest.WebServiceRequestBuilder builder = WebServiceRequest.builder()
                .serverHost(serverHost)
                .contentType(defaultContentType)
                .urlEncodingEnabled(urlEncodingEnabled);

        return builder;
    }

    @Override
    public WebServiceRequest.WebServiceRequestBuilder createInstance(Map<String, Object> parameters) {
        return createInstance();
    }

    @Override
    public WebServiceRequest.WebServiceRequestBuilder createAuthorizedInstance(String username) {
        return createInstance();
    }
}
``` 
This class is to create WebServiceRequestBuilder instance which contains information of HTTP requests including methods, URL, parameters, body content etc.
You can add your own initialization code for WebServiceRequestBuilder here.

*PostSocketIOClient.java*
```
@Slf4j
public class PostSocketIOClient extends SocketIOClient {

    public PostSocketIOClient(Socket socket) {
        super(socket);
    }

    @Override
    public void bindCustomEmitterListeners() {
        LOGGER.debug("bind custom emitter listeners");
        
        this.socket.on("Custom Action", objects -> {
            LOGGER.debug("SocketIO Custom Action");
        });

        this.socket.on(Socket.EVENT_RECONNECTING, objects -> {
            LOGGER.debug("SocketIO Custom Action2 : {}", objects);
        });
    }
}
```
In the SocketIOClient, you should bind your own EmitterListers to process messages from socket-io server. 

*PostSocketIOClientFactory.java*
```
@Component
public class PostSocketIOClientFactory extends AbstractSocketIOClientFactory {

    @Override
    public SocketIOClient createSocketIOClient(Socket socket) {
        return new PostSocketIOClient(socket);
    }
}
```
You can also create and instantiate your own SocketIOClient from the SocketIOClientFactory.

## Installation

TDB

## API Reference

TDB

## Tests
Run all test cases with specific profile
```sh
mvn -p[Profile] clean verify
```

Run specific test cases by using meta filter which is described with Meta information in story files
```sh
mvn -p[Profile] clean verify -Dmetafilter="+feature post"

mvn -p[Profile] clean verify -Dmetafilter="+scenario Creating a post"
```

## Contributors
Park, KyoungWook (Kevin) / sirius00@paran.com

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* TBD
