Meta: Verify party exist with mobile number

Narrative:
As a first time user
I want to start the validation process using my mobile number
So that I can onboard myself

Scenario: No party exists with the given mobile number
Given First time user with valid mobile number
When I start the validation process by making POST call to /party/searchbymobile
Then Service should return 200 with no record