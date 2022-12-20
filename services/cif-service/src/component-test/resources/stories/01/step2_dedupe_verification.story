Meta: Dedupe check

Narrative:
As a user I want to check if there is a dedupe
I want to perform validation on the requested data
If any data validation failed then sent the proper status code and message to the user
Otherwise, the send the total dedupe count


Scenario: User send the requested data and dedupe is found
Given User sends the valid requested data in the request parameter
When User started the dedupe process by making POST call to dedupe
Then 200 status code return with total dedupe count

Scenario: User send the requested data and no dedupe is found
Given User sends the required value
When Dedupe process started by making POST call to dedupe by the user
Then Status code 400 return with the error message