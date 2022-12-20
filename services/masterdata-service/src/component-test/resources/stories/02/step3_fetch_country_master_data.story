Meta: Fetch Country Master Data

Narrative:
User want to fetch country master data
API will return country master data else
Error response

Scenario: User send the valid request and record returned successfully
Given User sends the valid GET request
When User makes fetch api call for country master data api
Then Country master data fetch successfully and returns 200 status code with response data

Scenario: User send the valid request and no record returned
Given User makes a valid request
When Existing country master records to be deleted before fetching
When User start api call by making fetch api call to country master data api
Then API returns 204 status code as no record present in the database


