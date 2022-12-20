Meta: Create Party Guardian

Narrative:
I want to add party guardian details to the database
I want to perform validation on the requested data
If any data validation failed then send the proper status code and message to the user
Otherwise, the party guardian details created successfully

Scenario: User send the valid data and party guardian created successfully
Given User sends the valid guardian data in request
When User started the create party guardian process by making POST call to Party guardian
Then Party guardian Created Successfully and returns 201 status code

Scenario: User failed to create party guardian due to validation fail
Given User sends invalid guardian data request
When Validate the request parameters by making POST call to Party guardian
Then Service returns 400 status code and Failed to create Party guardian
