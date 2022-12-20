Meta: Updating party email id

Narrative:
As an existing user
I want to update my email id
If updation of email id failed then sent the proper status code and message to the user
Otherwise, the party email id updated successfully

Scenario: updated email id successfully
Given User sends valid data in request
When User validation process started by making PUT call to update emailId
Then Party EmailId Update Success for partyId and returns 200 status code

Scenario: User failed to update email id
Given User sends request parameters with blank or invalid values
When Validate the request parameters by making PUT call to update emailId
Then Service returns Party EmailId Update Failed for partyId and return status code 400