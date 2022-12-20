
# CIF Syncer

To avoid having to manually update the data, we need to make sure those integrations talk to each other. This means syncing data between them. This is sometimes done through a manual process, or by exporting data from one source and importing it into another. But it is best if we look for automated processes: systems where changes in one are automatically synchronized with the other.

The CIF Syncer will help in maintaining the integrity of the data between the CIF and the downstream system in the client eco-system. Keeping Every integration in Sync.

## Process flow

[![Flow](https://github.com/x-finx/cif/blob/develop/docs/resources/CIF_Syncer_As_Producer.jpg)](https://github.com/x-finx/cif/blob/develop/docs/resources/CIF_Syncer_As_Producer.jpg)
[![Flow](https://github.com/x-finx/cif/blob/develop/docs/resources/Syncer.png))](https://github.com/x-finx/cif/blob/develop/docs/resources/Syncer.png)

## Features
Source systems would invoke the Create API to create a record under CIF services
CIF ID will be returned as a response to the API call 
CIF service post successful creation, will act as a Producer of the Kafka topic for the above event.
CIF syncer will invoke the end-systems API for creation of CIF record in their respective systems. 
End systems will provide success/failure as response to the API call. 
The above information will be captured in the Transaction table of the Syncer service.
For the Call-back process, there are two processes through which the same can be achieved: 
   - Syncer will act as a Producer of the Kafka topic for providing the status of event (create) at the end systems and this information can be subscribed by the source system to update the information at their end.  
   - The Source system will invoke the call-back API of the syncer service and if the update is available against the record, the same will be updated in the source systems

Also, a remarks/message column to identify the failure scenario due to downtime of syncer service or any of the end systems to complete the event. 

## Fault Tolerance

Fault Tolerance scenarios and the strategy to mitigate the same

- Kafka to CIF Syncer - Once the process is completed successfully, then only we are committing offset else we are not committing . If in process any issue arise from kafka side , will get record again as soon as will kafka is stable.
- CIF Syncer to Integration - We have implemented the retry logic and each time we get an issue(connectivity or accessiblity) the timeout duration is increased. Currently, we are keeping retry count as "3" with a timeout duration of 30 seconds and each time we are storing the "ERROR" with api message in transaction table.
