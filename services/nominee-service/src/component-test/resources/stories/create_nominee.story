Meta: Create nominees

Narrative:
User want to add nominee
API will perform mandatory field validation on request data
If no error occurred nominee will get created
else error message thrown to user

Scenario: User send the valid data and nominees created successfully
Given User sends the valid nominee data in the request
When  User started the create nominee process by making POST call to nominee api
Then  Nominee created successfully and returns 201 status code


Scenario: User not provided all mandatory data and error will be returned
Given User sends the invalid nominee data in the request
When  User started the create nominee process by making POST api call
Then  API will return error


