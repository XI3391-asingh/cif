Meta: View party address

Narrative:
As an existing user
I want to view address
If wrong partyId provided then no data return
otherwise Success message with status code 200 return

Scenario: Fetching party address success
Given Valid values are send by the user
When Validation process started for getting the address by making GET call to party address
Then Success message and returns 200 status code

Scenario: Fetching party address failed
Given Invalid data are send by user in request parameter
When Validate the request parameters for getting the address by making GET call to party address
Then No record found and returns 404 status code