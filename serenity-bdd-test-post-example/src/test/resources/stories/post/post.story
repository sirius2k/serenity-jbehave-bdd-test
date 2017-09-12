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
Given title '${post.title}', body '${post.body}' and userId '${post.userId}'
When User creates post
Then Created Post contains id '101', title '${post.title}', body '${post.body}' and userId '${post.userId}'

Scenario: Get a post
Meta:
@scenario Get a post
Given a post id '1'
When User request a post
Then server should return Post containing id '1', not empty title, not empty body and userId '1'

Scenario: Create and Get a post
Meta:
@scenario Create and get a post
Given title '${post.title}', body '${post.body}' and userId '${post.userId}'
And User creates post
When User request a post with id '1'
Then server should return Post containing id '1', not empty title, not empty body and userId '1'