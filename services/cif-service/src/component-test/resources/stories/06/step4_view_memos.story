Meta: View party memos

Narrative:
As an existing user
I want to view memos
If wrong partyId provided then no data return
otherwise Success message with status code 200 return

Scenario: Fetching party memos successfully
Given Valid values are send in the request parameter
When Validation process started for getting the memos by making GET call to party memos
Then Success message and 200 status code return

Scenario: Fetching party memos failed
Given User did not send the required values in request parameter
When Validate the request parameters for getting the memos by making GET call to party memos
Then If no party is present with that party id then no data return