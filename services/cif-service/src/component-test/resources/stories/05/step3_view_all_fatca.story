Meta: View all party Fatca Details

Narrative:
As an existing user
I want to view all Fatca Details of the party
If wrong partyId provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all fatca details of a party successfully
Given User sends the party id to request for fatca
When Validation process started by making GET call to party Fatca
Then If fatca details exist for party id then api will return 200 with response

Scenario: Fetching all party fatca will be failed
Given User provides party id which does not exist
When Validate the request parameters by making GET call to party fatca
Then If fatca details for party id is not present then return no data