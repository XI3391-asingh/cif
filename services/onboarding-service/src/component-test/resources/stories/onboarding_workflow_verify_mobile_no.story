Meta: Verify customer exist with mobile number

Narrative:
As a first time user
I want to start the onboarding process using my mobile number
So that I can onboard myself

Scenario: No customer exists with the given mobile number
Given that the user is not registered and we have started a workflow and they have a valid mobile number
When they make a POST call to /customer/verify-mobile
Then service should return 200