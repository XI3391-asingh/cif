Meta: Update nominees

Narrative:
User want to update nominee
API will perform mandatory field validation on request data
If no error occurred nominee will get created
else error message thrown to user

Scenario: User send the valid data and nominees updated successfully
Given User sends the valid nominee data in the request
When  User started the update nominee process by making PUT call to nominee api
Then  Nominee updated successfully and returns 200 status code


Scenario: User not provided all mandatory data and error will be returned
Given User sends the invalid nominee data in the request
When  User started the update nominee process by making PUT api call
Then  API will return error


