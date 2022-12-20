Meta: Create party risks

Narrative:
I want to add party risks to the database
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the party risks created successfully

Scenario: User send the valid data and party risks created successfully
Given User sends the valid party risks data in the request
When User started the create party risks process by making POST call to risks
Then Party Risks Created Successfully and returns 201 status code

Scenario: User failed to create party risks due to validation fail
Given User send invalid party risks data in request
When Validate the request parameters by making POST call to risks
Then Service returns 400 status code and Party Risks Created Failed

