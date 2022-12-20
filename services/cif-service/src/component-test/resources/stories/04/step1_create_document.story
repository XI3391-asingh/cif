Meta: Create Party Documents

Narrative:
I want to add party documents to the database
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the party documents created successfully

Scenario: User send the valid data and party documents created successfully
Given User sends the valid value in request
When User started the create party documents process by making POST call to party documents
Then Party documents Created Successfully and returns 201 status code

Scenario: User failed to create party documents due to validation fail
Given User sends invalid data in request
When Validate the request parameters by making POST call to party documents
Then Service returns 400 status code and Failed to create Party Documents
