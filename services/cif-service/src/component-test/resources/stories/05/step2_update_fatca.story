Meta: Updating Party Fatca Details

Narrative:
As an existion user
I want to update my party fatca details
If updation of fatca Details failed then sent the proper status code and message to the user
Otherwise, the party fatca details updated successfully


Scenario: Update fatca successfully
Given User will send a valid value request
When Validation process started by making call by PUT method to party Fatca
Then Party Fatca Update Success for partyId and returns 200 status code

Scenario: User failed to update fatca details
Given User would send an invalid value request
When Validate the request parameters by making PUT call to party Fatca
Then Service returns status code 400 when no record of party Fatca Details exist