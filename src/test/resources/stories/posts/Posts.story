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
When User create post with title '${post.title}', body '${post.body}' and userId '${post.userId}'
Then Created Post contains id '101', title '${post.title}', body '${post.body}' and userId '${post.userId}'

Scenario: Showing a post
Meta:
@scenario Showing a post
When User request a post with id '1'
Then A returned Post contains id '1', not empty title, not empty body and userId '1'

Scenario: Creating and Showing a post
Meta:
@scenario Creating and Showing a post
When User create post with with title '${post.title}', body '${post.body}' and userId '${post.userId}' and request a post with id '1'
Then A returned Post contains id '1', not empty title, not empty body and userId '1'