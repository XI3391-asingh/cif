Meta: Create Party Memos

Narrative:
I want to add party memos to the database
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the party memos created successfully

Scenario: User send the valid data and party memos created successfully
Given User sends the required value in request
When User started the create party memos process by making POST call to party memos
Then Party memos Created Successfully and returns 201 status code

Scenario: User failed to create party memos due to validation fail
Given User did not send the required value in request
When Validate the request parameters by making POST call to party memos
Then Service returns 400 status code and Failed to create Party Memos
