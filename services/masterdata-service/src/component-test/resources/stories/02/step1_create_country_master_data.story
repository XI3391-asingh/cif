Meta: Create Country Master

Narrative:
User want to add country master data
API will perform mandatory field validation on request data
If no error occurred master data will get created
else error message thrown to user

Scenario: User send the valid data and country master data created successfully
Given User sends the valid value in the request
When User started the create country master data process by making POST call to country master data api
Then Country master data created successfully and returns 201 status code


Scenario: User sends invalid country master data
Given User sends the invalid country master data in the request
When User started the create country master data process by making POST api call
Then API will return error message with status code 400


