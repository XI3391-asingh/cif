Meta: View contacts

Narrative:
As an existing user
I want to view contacts
If wrong partyId provided then no data return
otherwise Success message with status code 200 return

Scenario: Fetching contact success
Given Valid contact values send by the user for fetch
When Validation process started for getting the contact by making GET call to contacts
Then Contact data and returns 200 status code

Scenario: Fetching contact failed
Given Invalid contact data is send by user in request parameter for fetch
When Validate the request parameters for getting the contact by making GET call to contacts
Then If party id not present in the database return no contact data found