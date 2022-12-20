Meta: Create Party Fatca

Narrative:
I want to add party fatca details to the database
I want to perform validation on the requested data
If any data validation failed then send the proper status code and message to the user
Otherwise, the party fatca details created successfully

Scenario: User send the valid data and party fatca created successfully
Given User sends the valid data in request
When User started the create party fatca process by making POST call to Party Fatca
Then Party Fatca Created Successfully and returns 201 status code

Scenario: User failed to create party fatca due to validation fail
Given User sends invalid data request
When Validate the request parameters by making POST call to Party Fatca
Then Service returns 400 status code and Failed to create Party Fatca
