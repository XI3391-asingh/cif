Meta: Delete nominee mapping

Narrative:
User want to delete nominee mapping
API will perform delete operation and if no error occurred return success

Scenario: User send the valid data and nominee mapping deleted successfully
Given User send the valid nominee id in the  parameter
When  User started the delete nominee mapping process by making DELETE call to nominee mapping api
Then  Nominee mapping deleted successfully and returns 200 status code


