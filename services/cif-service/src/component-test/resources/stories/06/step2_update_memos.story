Meta: Updating Party Memos

Narrative:
As an existing user
I want to update my party memos
If updation of party memos failed then sent the proper status code and message to the user
Otherwise, the party memo updated successfully

Scenario: Updated memos successfully
Given Valid value are send by the user in request parameter
When Validation process started by making PUT call to party memos
Then Party Memos Update Success for partyId and returns 200 status code

Scenario: User failed to update memo
Given Invalid value are send by the user in request parameter
When Validate the request parameters by making PUT call to party memos
Then Service returns status code 400 when no record of party memos exist