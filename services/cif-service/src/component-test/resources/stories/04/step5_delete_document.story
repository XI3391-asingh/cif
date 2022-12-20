Meta: Deleting Party Documents

Narrative:
As an existing user
I want to delete my party documents
If deletion of party documents failed then sent the proper status code and message to the user
Otherwise, the party documents deleted successfully


Scenario: Delete documents successfully
Given User has sent valid value in request
When Validation process by making DELETE call to party document
Then Party Documents Delete Success for partyId and returns 200 status code

Scenario: User failed to delete document
Given User has sent invalid value in request
When Validate the request parameters by making DELETE call to party documents
Then Service returns status code 400 when no record of party document exist