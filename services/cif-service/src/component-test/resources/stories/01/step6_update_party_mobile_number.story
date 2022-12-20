Meta: Updating party mobile number

Narrative:
As an existing user
I want to update my mobile number
If updation of mobile number failed then sent the proper status code and message to the user
Otherwise, the party mobile number updated successfully

Scenario: updated mobile number successfully
Given User send valid value in request
When User validation process started by making update mobile number api call
Then Party Mobile Update Success for partyId and returns 200 status code

Scenario: User failed to update mobile number
Given User send request with invalid mobile number
When Validate the request parameters by making update mobile number api call
Then Service returns Party Mobile Update Failed for partyId and return status code 400