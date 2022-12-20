Meta: View party document

Narrative:
As an existing user
I want to view documents
If wrong partyId provided then no data return
otherwise Success message with status code 200 return

Scenario: Fetching party documents success
Given Valid values are send by user
When Validation process started for getting the document by making GET call to party documents
Then Success message with status code 200 return

Scenario: Fetching party documents failed
Given Invalid values are send by the user in request parameter
When Validate the request parameters for getting the document by making GET call to party documents
Then If party id is not present in the database return no data