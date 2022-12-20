Meta: Deleting Party Guardian

Narrative:
As an existing user
I want to delete my party guardian
If deletion of party guardian failed then sent the proper status code and message to the user
Otherwise, the party guardian deleted successfully


Scenario: Delete guardian successfully
Given User has sent valid guardian value in request
When Validation process by making DELETE call to party guardian
Then Party guardian Delete Success for partyId and returns 200 status code

Scenario: User failed to delete guardian
Given User has sent invalid guardian value in request
When Validate the request parameters by making DELETE call to party guardian
Then Service returns status code 400 when no record of party guardian exist