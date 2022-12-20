Meta: View party risk

Narrative:
As an existing user
I want to view party risk
If wrong partyId provided then no data return
otherwise Success message with status code 200 return

Scenario: Fetching party risk success
Given Valid party risk values are send by the user
When Validation process started for getting the party risk by making GET call to party risk
Then Success message for party risk and returns 200 status code

Scenario: Fetching party risk failed
Given Invalid party risk data are send by user in request parameter
When Validate the request parameters for getting the party risk by making GET call to party risk
Then If party id not present in the database return no data for party risk