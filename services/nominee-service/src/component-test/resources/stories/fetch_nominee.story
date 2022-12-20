Meta: Fetch nominees

Narrative:
User want to fetch nominee
API will return nominee data based on nominee id

Scenario: User send the valid nominee id and nominees record returned successfully
Given User sends the valid nominee id in the request
When  User started the fetch nominee process by making POST call to nominee fetch api
Then  Nominee fetch successfully and returns 200 status code with response data

Scenario: User send the invalid nominee id
Given User sends the invalid nominee id in the request
When  User started the fetch nominee process by making api call to nominee fetch api
Then  Nominee fetch successfully and returns 204 status code with response data


