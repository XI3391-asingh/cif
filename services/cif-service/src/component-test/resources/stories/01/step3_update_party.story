Meta: Updating party record

Narrative:
As an existing user
I want to update party record
If updation of party record failed then sent the proper status code and message to the user
Otherwise, the party record updated successfully

Scenario: updated party record successfully
Given User send valid data in request
When User started the update party process by making PUT call to party/update
Then Party Update Success for partyIdentifier and returns 200 status code

Scenario: User failed to update party record
Given User send request parameters with invalid or null values
When Validate the request parameters by making PUT call to party/update
Then Service returns Party Update Failed for partyId and return status code 400