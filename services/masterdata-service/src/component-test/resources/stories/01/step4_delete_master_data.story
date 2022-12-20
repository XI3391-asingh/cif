Meta: Delete master data

Narrative:
User want to delete master data
API will perform delete operation and if no error occurred return success

Scenario: User send the valid master data id and master data deleted successfully
Given User sends the valid master data id in the parameter
When Create a valid master data record
When User started the delete master data process by making DELETE call to master data api
Then Master data deleted successfully and returns 200 status code


Scenario: User send the invalid master data id
Given User sends the invalid master data id in the parameter
When User started the delete process by making DELETE call to master data api
Then API returns 400 status code

