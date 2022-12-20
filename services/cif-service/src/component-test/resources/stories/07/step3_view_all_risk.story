Meta: View all party risks

Narrative:
As an existing user
I want to view all party risks of the party
If wrong partyId provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all party risks success
Given User sends the required party risks data
When Validation process started by making GET call to risks
Then If party risks exist for party Id api will return 200 with response

Scenario: Fetching all party risks failed
Given User provided party risks data which not exist in database
When Validate the request parameters by making GET call to risks
Then If party risks for party id is not present then return no data