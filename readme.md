## Synopsis
This project is base project for BDD Test which is created on top of Serenity, Spring Framework and JBehave. The objective of this project is to create REST API and WebSocket integration test quickly.

## Code Example
Story which describes acceptance criteria
```text
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
```text
[Requirement]
Narrative of requirement

Example)
JSONPlaceHolder post requirements
In order to share and manage posts
As a User
I want to view, create, update and delete a post
```
And, you can also describe the narrative of each story in the story file as follows.
```text
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
```java
@Component
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
```java
@Slf4j
public class PostSocketIOClient extends SocketIOClient {
    public PostSocketIOClient(Socket socket) {
        super(socket);
    }

    @Override
    public void bindCustomEmitterListeners() {
        socket.on(GET_POST, objects -> {
            JSONObject json = (JSONObject)objects[0];

            addMessage(json);

            LOGGER.debug("getPost : {}", json);
        });
    }
}
```
In the SocketIOClient, you should bind your own EmitterListers to process messages from socket-io server. 

*PostSocketIOClientFactory.java*
```java
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

In order to run demo test, you have to install npm and Node.js. Please, refer to https://www.npmjs.com/get-npm.
For socket-io server, you need to install npm and node first. Then install socket.io in global.

```sh
$ npm install -g socket.io
```

And, link socket.io in the src/test/java/resources/server. in both serenity-bdd-test-post-example and serenity-bdd-test-common project.

```sh
$ cd src/test/java/resources/server
$ npm link socket.io
```

For background running of the server you need to install forever.

```sh
$ npm install -g forever
```

## API Reference

serenity-bdd-test-common project provide nodejs server which provides SocketIO protocol for test. You can see server source at [server.js](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-common/src/test/resources/server/server.js). It serves the following SocketIO events.

- echo : echo will return the same message sent from client with 'echoBack' event.
- getPost : return post object with 'getPost' event
```json
    {
        id : 'id',
        title : 'title',
        body : 'test body',
        userId : 1
    }
```
- getBookStore : return bookstore object ([bookstore.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-common/src/test/resources/server/bookstore.json)) with 'getBookStore' event.
- getBook :  : return bookstore object (([book.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-common/src/test/resources/server/book.json))) with 'getBook' event.


serenity-bdd-test-post-example project also provide nodejs server which provides REST API and WebSocket protocol for test. You can see server source at [app.js](https://github.com/sirius2k/serenity-jbehave-bdd-test-post-example/blob/master/serenity-bdd-test-post-example/src/test/resources/server/app.js). It serves the following REST API and SocketIO events.
- GET / : just return hello text
- GET /posts : return posts object ([posts.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-post-example/src/test/resources/server/posts.json))
- GET /posts/:id : return post object ([post.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-post-example/src/test/resources/server/post.json)) with id
- POST /posts : return post object ([post.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-post-example/src/test/resources/server/post.json)) with id 101
- getPost : return post object ([post.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-post-example/src/test/resources/server/post.json)) with 'getPost' event
- getPosts : return post object ([posts.json](https://github.com/sirius2k/serenity-jbehave-bdd-test/blob/master/serenity-bdd-test-post-example/src/test/resources/server/posts.json)) with 'getPosts' event

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

This project is licensed under the MIT License