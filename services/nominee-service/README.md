
# Nominee Service

## SUMMARY 

Nomination is the service that enables the account holder or any other financial instrument investor to name someone to claim the deposit or the investment after the original owner of the asset or account passes away. Therefore, the nominee is the person (or, in some cases, a firm) that the person has mentioned in the section of nomination for the asset or account that they are the legal holders of. 

There will be a single nominee associated to an account except for cases of jointly operated locker accounts. 

## Need Statement

It isn't mandatory but it's always advisable to update the nominee on all your accounts including Term / Fixed,  Savings and the overall value you keep / invest with the bank. If a nomination is in place, the bank would simply pay-off the amount lying in deceased's account to the nominated person. The process of nomination is a critical task and it causes ease of settlement of the funds of the actual account holder.

It's always recommended to mention a nominee during the application for a new deposit or bank account. The process of nomination can process the person's assets to their nominee much easier in the event of death. 

One can View/Add/Change a nominee for their Fixed / Recurring Deposit in the following ways:  
1. On Mobile Banking  
    - Go to Banking  
    - Fixed/Recurring Deposits  
    - Nomination Update  
2. On Net Banking  
    - Go to Investment 
    - Deposits
    - Nomination Update  
3. Alternatively one can place this request at nearest branch as well.  


## Project Scope

Below is the list of features included as part of the scope: 

- Creation of a Nominee record  
- Update of Nominee record 
- Fetching the Nominee details 
- Deletion of a Nominee record 
- Nominee Mapping with Account 

A detailed description of the of the individual functionalities is provided in the Functional requirement section of the document 


## Functional Requirement

### Creation of a Nominee Record

A nominee in bank, is someone who has been designated in the bank application as the person who would receive the proceeds of the account in the case of unexpected death. Need to have a functionality to create the Nominee record associated to an account. If the Nominee is a minor, then there has to be Guardian details associated to the Nominee record. Below is the list of attributes to be captured while creating a Nominee record 

- Party ID 
- Nominee ID
- Salutation 
- First Name 
- Middle Name 
- Last Name 
- Relation 
- National ID 
- Nominee Guardian ID (In case the Nominee is a Minor)

### Update of Nominee Record

Functionality to update the Nominee record, in case of change in any updateable attribute.  

##### Use Case: 

- Any change in any attribute associated to the Nominee ID 
    - Update will be executed against the Nominee ID along with the specific attributes 


### Fetching a Nominee Record

Functionality to search a Nominee record basis the Nominee ID.  

##### Use Case: 

- To view and verify the complete details of a particular Nominee 
    - Search will be executed basis the Nominee ID as mandatory [Fetch] 


- To view all the Nominees associated to a particular Party 
    - Search can be executed in a combination of Party ID [Fetch All] 

### Deletion of a Nominee Record

Functionality to delete the Nominee record, in case there is a change in the mapping of the Nominee for a Party. Deletion will be executed for a particular Nominee ID. 

##### Use Case: 

 - To change the Nominee currently associated to the account 
     - Delete will be executed basis the Nominee ID

### Nominee Mapping with Account

The Party record will be created in the following sequence: 
Party Table --> Account --> Nominee
The relationship between the Party Account and Nominee alonwith %share will be maintained separately in a mapping table
Below are the list of attributes to be maintained in the mapping table
 - Party ID
 - Account Number
 - Nominee ID

## How to start nominee-service

Make sure you are in the `nominee-service` directry.

```
cd nominee-service
```

Run `./gradlew build` to build your service.

Connect to your local postgres instance. This assumes you have started Postgres using Docker Compose setup in `local`
directory.

```
psql postgresql://postgres:postgres@localhost:5432/postgres
```

Next, create the database

```
create database nomineeservicedb;
```

Quit psql

```
\q
```

Run the db migration script.

```
java -jar build/libs/nominee-service-1.0-SNAPSHOT-all.jar db migrate config.yml
```

Start the application by running following command

```
java -jar build/libs/nominee-service-1.0-SNAPSHOT-all.jar server config.yml
```

To check that your application is running enter url `http://localhost:12525`

## Health Check

To see your application's health enter url `http://localhost:12526/healthcheck`

## Docker image

```
docker build -t com.cif/nominee-service .
```

Run the docker image

```
docker run -p 12525:12525 com.cif/nominee-service
```

## Documents
| Name | Description |
| ------------------------- | ------ |
| [Attributes] | List of given features in the module with sample values for LOV fields and description across all fields |
| [Endpoints] | Detailed description on module end points and their respective specifications |

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)
   [Attributes]: <./docs/Field%20List.md>
   [Endpoints]: <./docs/Endpoints.md>
