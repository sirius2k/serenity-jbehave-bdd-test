JSONPlaceHolder posts

Meta:
@Author Kyoungwook Park
@feature posts

Narrative:
In order to test sample posts
As an Developer
I want to request Listing resources, Showing a resource, Creating a resource and Updating a resource

Scenario: Creating a post
Meta:
@scenario Creating a post
@clearContext
Given title '${post.title}', body '${post.body}' and userId '${post.userId}'
And User1 logged in
When User1 creates post
Then Created Post contains id '101', title '${post.title}', body '${post.body}' and userId '${post.userId}'

Scenario: Get a post
Meta:
@scenario Get a post
@clearContext
Given a post id '1'
And User1 logged in
When User1 request a post
Then server should return Post containing id '1', not empty title, not empty body and userId '1'

Scenario: Create and Get a post
Meta:
@scenario Create and get a post
@clearContext
Given title '${post.title}', body '${post.body}' and userId '${post.userId}'
And User1 logged in
And User1 creates post
When User1 request a post with id '1'
Then server should return Post containing id '1', not empty title, not empty body and userId '1'

Scenario: Get a post from websocket
Meta:
@scenario Get a post from websocket
@clearContext
Given a post id '1'
And User1 logged in
When User1 request a post with id '1' from websocket
Then server should return Post containing id '1', not empty title, not empty body and userId '1'
