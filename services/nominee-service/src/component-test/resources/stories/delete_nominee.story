Meta: Delete nominees

Narrative:
User want to delete nominee
API will perform delete operation and if no error occurred return success

Scenario: User send the valid party id and nominees deleted successfully
Given User sends the valid nominee id in the parameter
When  User started the delete nominee process by making DELETE call to nominee api
Then  Nominee deleted successfully and returns 200 status code

Scenario: User send the invalid party id
Given User sends the invalid nominee id in the parameter
When  User started the delete nominee process by making api call to nominee delete api
Then  Returns error code 400 response

