Meta: Delete country master data

Narrative:
User want to delete country master data
API will perform delete operation and if no error occurred return success

Scenario: User send the valid country master data id and country master data deleted successfully
Given User sends the valid country master data id in the parameter
When Create a valid country master data record
When User started the delete country master data process by making DELETE call to country master data api
Then Country Master data deleted successfully and returns 200 status code


Scenario: User send the invalid country master data id
Given User sends the invalid country master data id in the parameter
When User started the delete process by making DELETE call to country master data api
Then Error message with status code 400 return

