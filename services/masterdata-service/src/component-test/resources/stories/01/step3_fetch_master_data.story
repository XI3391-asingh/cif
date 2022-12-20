Meta: Fetch Master Data

Narrative:
User want to fetch master data
API will return master data else
Error response

Scenario: User send the valid master type and record returned successfully
Given User sends the valid master type in the request
When User makes fetch api call for master data api
Then Master data fetch successfully and returns 200 status code with response data

Scenario: User send the valid master type and no record returned
Given User send the valid master type in the request
When Existing records to be deleted before fetching
When User start api call by making fetch api call to master data api
Then API returns 204 status code as no record present in database


