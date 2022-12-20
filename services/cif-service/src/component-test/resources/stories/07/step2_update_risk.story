Meta: Updating party risk

Narrative:
As an existing user
I want to update my party risk
If updation of party risk failed then sent the proper status code and message to the user
Otherwise, the party risk updated successfully

Scenario: updated party risk successfully
Given Valid risk data is send by the user
When Validation process started by making PUT call to party risk
Then Party risk Update Success for partyId and returns 200 status code

Scenario: User failed to update risk
Given Invalid risk data send by user in request parameter
When Validate the request parameters by making PUT call to party risk
Then Service returns status code 400 when no record of party risk exist