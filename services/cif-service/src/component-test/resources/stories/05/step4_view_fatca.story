Meta: View Party Fatca Details

Narrative:
As an existing user
I want to view party fatca details
If wrong partyId provided then no data return
otherwise Success message with status code 200 return

Scenario: Fetching party fatca on success
Given Valid party fatca values sent by the user
When Validation process started for getting the party fatca details by making GET call to party Fatca
Then Success message for party fatca and returns 200 status code

Scenario: Fetching party fatca gets failed
Given Invalid party fatca data factors are sent by user in request
When Validate the request for getting the party fatca factors by making GET call to party fatca
Then If data factors not present in the database return no data for party fatca
