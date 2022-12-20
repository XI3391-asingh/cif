Meta: Create contacts

Narrative:
I want to add contact to the database
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the contact created successfully

Scenario: User send the valid data and contact created successfully
Given User sends the valid contact data in the request
When User started the create contact process by making POST call to contact
Then Contact Created Successfully and returns 201 status code

Scenario: User failed to create contact due to validation fail
Given User send invalid contact data in request
When Validate the request parameters by making POST call to contact
Then Service returns 400 status code and Contact Created Failed

