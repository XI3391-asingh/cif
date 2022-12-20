Meta: View all party memos

Narrative:
As an existing user
I want to view all memos of the party
If wrong partyId provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all memos of party successfully
Given User send the valid party id in parameter
When Validation process started by making GET call to party memos
Then If memos exist for party id then api will return 200 status code with response

Scenario: Fetching all party memos failed
Given User invalid party id in request parameter
When Validate the request parameters by making GET call to party memos
Then If memos for party id is not present then return no data