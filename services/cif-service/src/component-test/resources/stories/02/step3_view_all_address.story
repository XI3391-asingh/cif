Meta: View all party address

Narrative:
As an existing user
I want to view all address of the party
If wrong partyId provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all address of party success
Given User sends the required data
When Validation process started by making GET call to party address
Then If Address exist for party Id api will return 200 with response

Scenario: Fetching all party address failed
Given User provided data which not exist in database
When Validate the request parameters by making GET call to party address
Then If address for party id is not present then return no data