Meta: Create Master Data For Lookup Master and Address Master

Narrative:
User want to add master data for lookup master and address master
API will perform mandatory field validation on request data
If no error occurred master data will get created
else error message thrown to user

Scenario: User send the valid data and master data created successfully
Given User sends the valid data in the request
When User started the create master data process by making POST call to master data api
Then Master data created successfully and returns 201 status code


Scenario: User sends invalid data
Given User sends the invalid master data in the request
When User started the create master data process by making POST api call
Then API will return error with status code 400


