Meta: View all party documents

Narrative:
As an existing user
I want to view all documents of the party
If wrong partyId provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all documents of party successfully
Given User sends the required value in parameters
When Validation process started by making GET call to party documents
Then If documents exist for party id then api will return 200 with response

Scenario: Fetching all party documents failed
Given User provides data which does not exist in database
When Validate the request parameters by making GET call to party documents
Then If documents for party id is not present then return no data