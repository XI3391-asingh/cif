Meta: Update Party Record For Soft Delete

Narrative:
Party got created earlier
Now Party record in system is not needed so marking that record for soft delete
Once flag for soft delete success api return 200 success code

Scenario: Update Party record for soft-delete when record exist
Given Provide party id for soft delete
When  Call soft-delete api for party record flag update
Then  Api will return success response when flag update completed

Scenario: Update Party record for soft-delete when record not exist
Given Provide party record for soft delete
When  Call /soft-delete api for party record flag update
Then  Api will return no content response when record not exist in database