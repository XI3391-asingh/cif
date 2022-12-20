Meta: Updating party address

Narrative:
As an existing user
I want to update my party address
If updation of contact address failed then sent the proper status code and message to the user
Otherwise, the party address updated successfully

Scenario: updated contact number successfully
Given Valid data is send by the user
When Validation process started by making PUT call to party address
Then Party Address Update Success for partyId and returns 200 status code

Scenario: User failed to update contact number
Given Invalid data send by user in request parameter
When Validate the request parameters by making PUT call to party address
Then Service returns status code 400 when no record of party address exist