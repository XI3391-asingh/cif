Meta: Find customer information based on passed cif id

Narrative:
User had created customer
User want to see all customer related information
So that it can validate or update previous stored information

Scenario: No party exists with the given cif ids
Given User with cif id which not exist
When User fetch all party record by making GET call to /party/
Then Fetch service should return 404 with no record

Scenario: Party exists with the given cif ids
Given User with cif id which exist
When User fetch all party information by making GET call to /party/
Then Fetch service should return 200 with record