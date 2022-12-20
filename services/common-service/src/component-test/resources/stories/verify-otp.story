Meta:

As a unregistered user
I want to enter my mobile number
and receive an OTP for registration

Scenario: user enters mobile number and otp for verification
Given that the user enters mobile number and otp for verification
When they make a post call to /otp/verify
Then service should return 200 response
