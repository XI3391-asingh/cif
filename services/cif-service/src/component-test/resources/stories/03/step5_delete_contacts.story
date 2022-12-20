Meta: Deleting party contact

Narrative:
As an existing user
I want to delete my contact number
If deletion of contact number failed then sent the proper status code and message to the user
Otherwise, the party contact number delete successfully

Scenario: deleted contact number successfully
Given User send valid data parameter in request for deletion
When Validation process started by making DELETE call to party contact
Then Party Contact deletion Success for partyId and returns 200 status code

Scenario: User failed to delete contact number
Given User send request parameters which is invalid data
When Validate the request parameters by making DELETE call to party contact
Then Service returns Party Contact Delete Failed and return status code 400