Meta: Update country master data

Narrative:
User want to update country master data
API will perform mandatory field validation on request data
If no error occurred country master will get updated
Else error message thrown to user

Scenario: User send the valid data and country master data updated successfully
Given User sends the valid country master data in the request
When User started the update country master data process by making PUT call to country master data api
Then Country master data updated successfully and returns 200 status code

Scenario: User send the invalid data and country master data updation failed
Given User sends the invalid country master data id in the request
When User started the update process by making PUT call to country master data api
Then Country master data updated failed and returns 400 status code



