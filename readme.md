## Synopsis
This project is base project for BDD Test which is created on top of Serenity, Spring Framework and JBehave. 

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
TDB