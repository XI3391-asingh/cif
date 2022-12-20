Meta: Create Admin Data

Narrative:
I want to add admin data to the database
I want to perform validation on the requested data
If any data validation failed then send the proper status code and message to the user
Otherwise, the admin data created successfully

Scenario: User send the valid data and admin data created successfully
Given User sends the valid admin data in request
When User started the create admin process by making POST call to admin API
Then Admin Data Created Successfully and returns 201 status code

Scenario: User failed to create admin data due to validation fail
Given User sends invalid admin data request
When Validate the request parameters by making POST call to admin API
Then Service returns 400 status code and Failed to create admin data
