Meta: Create party address

Narrative:
I want to add party address to the database
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the party address created successfully

Scenario: User send the valid data and party address created successfully
Given User sends the valid value in the request
When User started the create party address process by making POST call to party address
Then Party Address Created Successfully and returns 201 status code

Scenario: User failed to create party address due to validation fail
Given User send invalid data in request
When Validate the request parameters by making POST call to party address
Then Service returns 400 status code and Party Address Created Failed

