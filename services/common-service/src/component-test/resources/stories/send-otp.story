Meta:

Narrative:
As a unregistered user
I want to enter my mobile number
and receive an OTP for registration

Scenario: user is generating an otp from the system
Given that the user enters mobile number for registration
When they make a post call to /otp
Then service should return 200 response

Scenario: user is generating an otp from the system 5 times
Given that the user enters mobile number for registration
When they make a post call to /otp 5 times
Then service should send failure message