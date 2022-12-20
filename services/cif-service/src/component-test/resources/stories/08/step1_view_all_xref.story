Meta: View all party xref

Narrative:
As an existing user
I want to view all party xref of the party
If wrong partyIdentifier provided then no data will be return
otherwise Success message with status code 200 return

Scenario: Fetching all party xref success
Given User sends the valid party identifier
When Validation process started by making GET call to xref
Then If party xref exist for party identifier api will return 200 with response

Scenario: Fetching all party xref failed
Given User sends the invalid party identifier
When Validate the request parameters by making GET call to xref
Then Exception will be thrown and a exception message will be return