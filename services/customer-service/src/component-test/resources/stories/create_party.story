Meta: Create party record

Narrative:
I want to add party record to the database
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the party records created successfully

Scenario: User failed to create party record due to validation fail
Given User send request parameters with blank or invalid values
When Validate the request parameters by making POST call to /party
Then Service returns 422 status code input validation failed

Scenario: User send the valid data and party created record successfully
Given User send the valid data in request
When I saving party record in the database by making POST call to /party
Then Party Created Successfully and returns 202 status code