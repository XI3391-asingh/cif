Meta: View all contacts

Narrative:
As an existing user
I want to view all contacts of the party
If wrong partyId provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all contacts success
Given User sends the required contact data
When Validation process started by making GET call to contacts
Then If Contact exist for party Id api will return 200 with response

Scenario: Fetching all contacts failed
Given User provided contact data which not exist in database
When Validate the request parameters by making GET call to contacts
Then If contact for party id is not present then return no data