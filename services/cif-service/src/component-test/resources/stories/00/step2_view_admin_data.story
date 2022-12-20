Meta: View admin data
Narrative:
As an existing user
I want to view admin data
If wrong type provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching admin data success
Given User sends the valid type
When Validation process started by making GET call to admin
Then If admin data exist for the type provided then api will return 200 with response

Scenario: Fetching admin data failed
Given User sends the invalid type
When Validate the request parameters by making GET call to admin
Then Error message will be shown with status code 500