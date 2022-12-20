Meta: Deleting party address

Narrative:
As an existing user
I want to delete my party address
If deletion of address failed then sent the proper status code and message to the user
Otherwise, the party address deleted successfully

Scenario: deleted address successfully
Given Valid address input parameter is sent by user
When Validation process started by making DELETE call to party address
Then Party Address Deleted Successfully for partyId and returns 200 success status code

Scenario: User failed to delete address details
Given Invalid address input parameter sent by user
When Validate the request parameters by making DELETE call to party address
Then Service returns status code 400 when no record of party address does not exist