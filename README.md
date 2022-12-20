## Overview

This file gives you an understanding about the CIF microservices catering to different features pertaining to the CIF module and syncing this information with the multiple downstream system using the CIF syncer functionality.



## Documents

| Name | Description |
| ------------------------- | ------ |
| [BRD (CIF)]| Business Requirement Documentation, detailed information on the features and functions of the CIF module |
| [BRD (Syncer)] | Business requirement Documentation, detailed information on the features and functionalities of the Syncer module |
| [Attributes] | List of given features in the module with sample values for LOV fields and description across all fields |
| [Endpoints] | Detailed description on module end points and their respective specifications |
| [Getting started (CIF)] | This page is an overview of the CIF Developer documentation and related resources.|
| [Getting started (Syncer)] | This page is an overview of the Syncer Developer documentation and related resources.|


[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

   [BRD (CIF)]: <./docs/cif/BRD.md>
   [BRD (Syncer)]: <./docs/cif-syncer/BRD.md>
   [Attributes]: <./docs/cif/Field%20List.md>
   [Endpoints]: <./docs/cif/Endpoints.md>
   [Tools and Code Layout]: <https://github.com/x-finx/cif/blob/feature_documentation/docs/cif/Tools%20and%20Code%20Layout.md>
   [Getting started (CIF)]: <https://github.com/x-finx/cif/tree/develop/services/cif-service#cif-service>
   [Getting started (Syncer)]: <https://github.com/x-finx/cif/tree/develop/services/cif-syncer-service#cif-syncer-service>

## CIF Microservices Monorepo

This repository houses all the CIF Microservices and shared libraries.

Following is the list of Microservices

| S.No. | Service                                             | CI Status | Port  | Dev Swagger URL |
|-------|-----------------------------------------------------| --------- |-------| --------------- |
| 1     | [cif-service](./services/cif-service)               | To Add    | 11699 | To Add          |
| 2     | [cif-syncer-service](./services/cif-syncer-service) | To Add    | 14893 | To Add          |
| 3     | [nominee-service](./services/nominee-service)       | To Add    | 12525 | To Add          |


## Tools

Please install following tools

| Tool                                      | Version | Required |
| ----------------------------------------- | ------- | -------- |
| Java                                      | 17      | Yes      |
| IntelliJ Idea Community Edition: Java IDE | Latest  | Yes      |
| Markdown Editor MarkText / Typora(Paid)   | Latest  | Optional |
| Git                                       | Latest  | Yes      |
| VS Code: Code editor                      | Latest  | Optional |
| Docker                                    | Latest  | Yes      |
| Postgres                                  | 13.3    | Yes      |
| DBeaver: DB client                        | Latest  | Optional |
| Redis                                     | 6       | Yes      |
| Python                                    | 3.9     | Optional |
| CookieCutter                              | 1.7.3   | Optional |
| Talisman                                  | Latest  | Yes      |
| Elastic Search                            | 6.5.2   | Yes      |


You will find instructions to install some of the tools in the [local](./local) directory.


## Code Layout

Below are the important files and directories in this repositiory.

* [local](./local): This directory contains instructions to set the local development environment. It also includes docker-compose setup to start important services that Microservices require for development. This includes Postgres, Redis, and Kafka.  
* `gradle`: Each Microservice in this repository uses Gradle as build and dependency management tool. We have also added a root Gradle build script in case developers want to import all the Microservices and libraries in their IDE.
* [services](./services): This contains all the Microservices that we will build for cif
* [shared](./shared): This contains all the custom libraries that Microservices will use.
* [templates](./templates): It contains the Microservices starter template
* `tools`: This contains a Python script to instantiate new Microservices from Microservices template in the templates directory.
* `.talismanrc`: This file contains entries of files that we don't want talisman to scan. It will ignore the fileas long as the checksum of the file matches the value mentioned in the `checksum` field.
* `CODEOWNERS`: CODEOWNERS file is used to define individuals or teams that are responsible for code in a repository.


## Creating a new Microservice

You will use `msgen` tool to create a new Microservices. You can read the complete documentation for msgen in the [tools](./tools) directory.

First chnage directory tools and then run the `msgen` tool to create a new Microservice.

```
cd tools
./msgen.py --service
```

## Entity wise API list

|Entity     |API Supported | URL                                               |
|-----------|--------------|---------------------------------------------------|
|Address    |Create        | ```/party/{partyIdentifier}/address```                    |
|Address    |Update        | ```/party/{partyIdentifier}/address/{partyAddressId}``` |
|Address    |View          | ```/party/{partyIdentifier}/address/{partyAddressId}``` |
|Address    |ViewAll       | ```/party/{partyIdentifier}/address```                    |
|Address    |Delete        | ```/party/{partyIdentifier}/address/{partyAddressIdentifier}``` |
||||
|Contact    |Create        | ```/party/{partyIdentifier}/contacts```                    |
|Contact    |Update        | ```/party/{partyIdentifier}/contacts/{partyContactDetailsId}``` |
|Contact    |View          | ```/party/{partyIdentifier}/contacts/{partyContactDetailsId}``` |
|Contact    |ViewAll       | ```/party/{partyIdentifier}/contacts```                    |
|Contact    |Delete        | ```/party/{partyIdentifier}/contact/{contactIdentifier}``` |
||||
|Memos      |Create        | ```/party/{partyIdentifier}/memos```                       |
|Memos      |Update        | ```/party/{partyIdentifier}/memos/{partyMemosId}```      |
|Memos      |View          | ```/party/{partyIdentifier}/memos/{partyMemosId}```      |
|Memos      |ViewAll       | ```/party/{partyIdentifier}/memos```                       |
||||
|Risk       |Create        | ```/party/{partyIdentifier}/risks```                       |
|Risk       |Update        | ```/party/{partyIdentifier}/risks/{partyRiskId}```      |
|Risk       |View          | ```/party/{partyIdentifier}/risks/{partyRiskId}```      |
|Risk       |ViewAll       | ```/party/{partyIdentifier}/risks```                       |
||||
|Fatca      |Create        | ```/party/{partyIdentifier}/fatca```                      |
|Fatca      |Update        | ```/party/{partyIdentifier}/fatca/{partyFatcaDetailsId}```    |
|Fatca      |View          | ```/party/{partyIdentifier}/fatca/{partyFatcaDetailsId}```    |
|Fatca       |ViewAll       | ```/party/{partyIdentifier}/fatca```  
||||
| Documents |Create        | ```/party/{partyIdentifier}/documents```                   |
| Documents |Update        | ```/party/{partyIdentifier}/documents/{partyDocumentId}``` |
| Documents |View          | ```/party/{partyIdentifier}/documents/{partyDocumentId}``` |
| Documents |ViewAll       | ```/party/{partyIdentifier}/documents```                   |
| Documents |Delete        | ```/party/{partyIdentifier}/document/{documentId}``` |
||||
|Guardian      |Create        | ```/party/{partyIdentifier}/guardian```                      |
| Guardian |Delete        | ```/party/{partyIdentifier}/guardian/{partyGuardianId}``` |
||||
| Xref |ViewAll       | ```/party/{partyIdentifier}/xref```   
||||
| Dedupe |Create        | ```/party/dedupe```    
** Access to delete API should be restricted to Admin user using Role based access controls.
