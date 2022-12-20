Meta: Updating party status

Narrative:
As an existing user
I want to update party status
If updation of party status failed then sent the proper status code and message to the user
Otherwise, the party status updated successfully

Scenario: updated party status successfully
Given User send valid id value in request
When Validation process started by making update party status api call
Then Party Status Update Success for partyId and returns 200 status code

Scenario: User failed to update party status
Given User send request parameters with blank or invalid id value
When Validate the request by making update party status api call
Then Service returns Party Status Update Failed for partyId and return status code 400