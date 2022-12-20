Meta: Updating contacts

Narrative:
As an existing user
I want to update my contacts
If updation of contact failed then sent the proper status code and message to the user
Otherwise, the contact updated successfully

Scenario: updated contact successfully
Given Valid contact data is send by the user for updation
When Validation process started by making PUT call to contacts
Then Contacts Update Success for partyId and returns 200 status code

Scenario: User failed to update contact
Given Invalid contact data send by user in request parameter
When Validate the request parameters by making PUT call to contact
Then Service returns status code 400 when no record of contact exist