Meta: Create nominee mapping

Narrative:
User want to add nominee mapping
API will perform mandatory field validation on request data
If no error occurred nominee mapping will get created
else error message thrown to user

Scenario: User send the valid data and nominee mapping created successfully
Given User sends the valid data in the request
When  User started the create nominee mapping process by making POST call to nominee mapping api
Then  Nominee mapping created successfully and returns 201 status code


Scenario: User not provided all mandatory data and error will be returned
Given User sends the invalid data in the request
When  User started the create nominee mapping process by making POST api call
Then  Status code 400 will return


