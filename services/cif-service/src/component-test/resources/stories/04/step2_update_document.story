Meta: Updating Party Documents

Narrative:
As an existing user
I want to update my party documents
If updation of party documents failed then sent the proper status code and message to the user
Otherwise, the party documents updated successfully


Scenario: Updated documents successfully
Given User sends valid value in request
When Validation process started by making PUT call to party documents
Then Party Documents Update Success for partyId and returns 200 status code

Scenario: User failed to update document
Given User sends invalid value in request
When Validate the request parameters by making PUT call to party documents
Then Service returns status code 400 when no record of party documents exist