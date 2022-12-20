Meta: Update master data

Narrative:
User want to update data
API will perform mandatory field validation on request data
If no error occurred master will get updated
Else error message thrown to user

Scenario: User send the valid data and master data updated successfully
Given User sends the valid master data in the request
When User started the update master data process by making PUT call to master data api
Then Master data updated successfully and returns 200 status code

Scenario: User send the invalid data and master data updation failed
Given User sends the invalid master data id in the request
When User started the update process by making PUT call to master data api
Then Master data updated failed and returns 400 status code



