# CIF

Customer Information Files (CIFs) are e-files that store relevant data of the customers' personal information. The customer information file contains a unique CIF number, and it allows banks to access the data of their clients / prospects and customers. 

Customer information files are mainly used in the banking industry, and other sectors are gradually starting to use customer information files to store their customers' data. Customer information files at a specific bank typically include customer demographic information, account ownership information and credit relationships (if any). 

Customer information files act as a repository for verifying customer data without the need to view individual accounts and transactions. 

## Need Statement

CIFs are typically a central component of an integrated banking application package and are primarily used to support operational activities. With the advancement in technology have made maintaining customer information an easy task for the banks. There is no need to maintain physical records as they can be stored in electronic form.  

## Key Benefits

- This has reduced the cost of maintenance.
- No need for physical space and location.
- There is no need  for a workforce to store the paper documents.
- Easy to update and maintain Customer information files.
- Helps bank to upsell / cross-sell products and services.
- Also play a vital prelude in 'lead generation' business that uses purchase history. 

## Scope
- [Create Config Data](#create-config-data)
- [Create Record](#create-party)
- [Update Record](#update-party)
- [Search Record](#search-party)
- [Fetch Record](#fetch-party)
- [Dedupe](#dedupe-party)
- [Audit Trail](#audit-trail)
- [Soft Deletion](#soft-deletion)
- [Merge and Unmerge](#merge-unmerge)
- [Advanced Search](#advanced-search)
- [PII Data](#pii-data)

## Functional Requirement

<h3 id="create-config-data">Create Config Record</h3>

Only the Admin can create config data with the help of Admin API. Two types of config records can be created: one for Encryption type and the other for Universal Search Field. The config data must be set first with the help of Admin API before creating the party records so as to ensure which fields should be encrypted and which fields should be used in the universal search. The Config Data which can be set by Admin are described below:

### Encryption
Fields :  
  - party.firstName  
  - party.lastName  
  - party.primaryEmail  
  - party.middleName  
  - party.fullName  
  - party.primaryMobileNumber  
  - party.nationalId  
  - occupationDetail.taxId  
  - address.addressLine1  
  - address.addressLine2  
  - address.addressLine3  
  - contactDetails.contactValue  
  
EncryptionFlag: 
 - TRUE
 
### Universal Search Fields
Text:  
  - party.firstName  
  - party.middleName  
  - party.lastName  
  - party.fullName  
  - party.nickName  
  - party.mothersMaidenName  
  - party.primaryEmail  
  - party.amlRisk  
  - occupationDetail.companyTypeCode  
  - party.staffCode  
  - party.isDeleted  
  - party.amlCheckStatus  
  - partyFlag.isNpa  
  - address.ward  
  - address.city  
  - contactDetails.contactValue  
  
Number:  
  - party.primaryMobileNumber  
  - party.dateOfBirth  
  - occupationDetail.companyTypeCode  
  - party.staffCode  
  - address.cityZipCode  
  - contactDetails.contactValue  
  
Email:  
  - party.primaryEmail  
  
Date:  
  - party.dateOfBirth  
  
SpecialChar:  
  - party.dateOfBirth

<h3 id="create-party">Create Party record</h3>

As a consumer of the functionality, one can create a party record that typically includes customer demographic information. In general a record is a collection of fields, possibly of different data types. The consumer has to ensure that all the mandatory parameters or information are provided for creating the record. More details regarding the information are below: 

#### Demographics
- Name
- Gender
- Date of Birth
- Registered Mobile Number
- Registered Email ID

#### Address
- Address Type
- Address line 1
- Address line 2
- Address line 3
- City
- District
- Ward
- State
- Country

#### Contact Details
- Contact type (Mobile, Email, Social)
- Contact level (Primary, Secondary)
- Contact value (Mobile Number, Email ID)

#### Flags and Memos
- PEP (Politically Exposed)
- NPA (Non-Perfoeming Assets)
- FATCA Applicable
- Watchlist

#### KYC
- KYC Documents
- KYC status

#### Risk
- Risk Type (AML,KYC,Blacklisted)
- Risk Category (Low,Medium,High)
- Risk evaluadted date
- Risk valid upto

#### Employment details
- Occupation
- Industry Type
- Organization Name
- Job Position

#### FATCA
- Taxpayer Identification number (TIN)
- Country
- Identification type (TIN or Other)

#### Relation
- Relation Type (Father,Mother)
- Grouping of family members

#### Guardian
- Name
- Address
- Percentage share

#### Documents
- Document Type
- Document Number
- Document Issuance Date
- Document Issuance Place
- Document Expiry Date
- Document Reference Number

<h3 id="update-party">Update Party record</h3>
As a consumer of the functionality, one can update an existing party record that could be self-initiated or initiated by any user persona like agents, RM, or others. In general to update an existing party record, the record is identified by unique ID. An update could be 

 - Any set of editable fields and attributes 
 - Like status, 
 - Email, 
 - Mobile number 
 - Other demographics. 

It may offer the convenience to the consumer of editing for records or Updating custom object records. Moreover, it depends on the permissions someone has. 

* Not all fields of a record can be edited

<h3 id="search-party">Search Party record</h3>
As a consumer of the functionality, one can search an existing party record based on the search criteria provided. The search could be exact or like search. 

#### Exact Search: 
The search will display the result, In case the search criteria finds an exact match with an existing record. Like, 
- Mobile Number  
- Email ID  
- Date of Birth 

#### Like Search: 
The search will display the result, In case the search criteria finds a pattern match with an existing record. Like,  
- First name 
- Last name 
- Address 

The consumer can also perform below actions  

- Filtering: Filtering of the records based on the attributes available in the list  
- Sorting: Sorting of the records (Ascending / Descending) by either selecting the field name available in the list. Default sorting will be applied on Party ID in ascending order. 
- Pagination: Number of records that will be visible in a single page. It can be set to a value of n any positive integer value. The result will also include the total pages count.  

#### Search Result
- Party Identifier (CIF)
- Full Name
- Mothers Name
- Date of Birth
- Status
- Contact Detail (Mobile number and Email id)
- Citizenship

<h3 id="fetch-party">Fetch Party record</h3>

As a consumer of the functionality, one can fetch the existing party record based on the unique parameter party ID provided. In case no party record found against the provided party ID, It will result an empty response data. 

The fetch result includes: 

- Basic Customer Information, Flags, Party Memos – Information on party demographics along with flags and memo details.  
- KYC and Risks – Information of the KYC updated for the party and information on other risks like AML and blacklisted  
- Address including Preferences – Information of all the address associated to the party along with its preferences  
- Contacts including Preferences - Information of all the Contact channels along with preferences mapped to the party  
- FATCA - CRS Details – Information on the FATCA and CRS associated to the party  
- Employment Details – Employment details of the party  
- Relation View – Information of all the related parties and the relationship between the parties  
- Device/Channel Information (Mobile App/ POS/ Net banking) - Information of all the communication channels used by the party along with its status and last usage date.  
- Customer Audit – Information of all the history of changes associated to the party record  
-  Guardian – Information of Guardian in case the party is a minor 

<h3 id="dedupe-party">Dedupe Party record</h3>
Dedupe is a process to remove duplicate entries from a list or database. By removing multiple duplicate copies of data from a database or list, the process significantly decreases storage requirements and decreases the amount of data needing to be backed up. 

As a consumer of the functionality, one can find the possible dedupes record. In recent years banks have built policies to prevent the dedupe. Dedupes are identified on the basis of rules defined by the bank. Dedupe rules can be derived from a combination of one or more attributes. 
- Mobile number with Country code
- National ID with National ID type
- DOB with any one of the above parameters


#### Dedupe Rules: 

##### Rule 1: Mobile Number 

In many scenarios, Phone number is the most prevalent as any other attribute to identify the Deduplication. Many of the contacts don't have mail ids and other information’s but have phone numbers. it's important to make sure no duplicate records with same phone numbers are added. 

**Best Practice** is to use the Mobile number in combination with the ISD code, when the presence is across the geography to attain the optimum Dedupe result. 
If secific to a country then Mobile number can be used as a stand-alone parameter for dedupe

##### Rule 2: National ID 

In many scenarios, the Phone number could be different for same customer in that scenario the National ID (Unique) to identify the Deduplication. Since many of the customer uses dual SIM capability offered by Mobile devices. it's important to make sure no duplicate records with same National ID added.  

##### Rule 3: National ID & ID Type  

In many scenarios, the National ID could be different for same customer considering the multiple type of National ID supported by the system. In that scenario It is important to identify the Deduplication  for the National ID (Unique) and the ID type. 

**Best Practice** is to consider adding Nationality of the party along with National ID where National ID Type = Passport to find the optimum Dedupe result

##### Rule 4: Full Name and Date of Birth 

In addition to the above scenarios, combination of Full Name and Date of Birth will make sure no duplicate records added.

<h3 id="audit-trail">Audit Trail</h3>
An audit trail is a date and time-stamped, sequential record of the history and details around a financial transaction, work event, or financial ledger entry. 

It’s important for businesses to maintain a comprehensive and complete audit trail so that they can track back any irregularities and find process breakdowns if and when they happen. An airtight audit trail helps companies identify internal fraud by keeping track of the different users and the actions they take with regard to a company’s data and information.  

Audit trail records can also help identify outside data breach issues. Malware and ransomware crimes are on the rise, and tracking an audit trail can help identify and flag moments where outsiders are looking to do harm, while simultaneously improving your company’s information security capabilities.  

#### Summary
It  will provide the list of all the changes made to the Party record in a descending order with the latest change displayed first. Fields displayed in the listing will be
- Trail ID  
- Event Type  
- Modified Attribute  
- Modified At  
- Modified by Channel  

#### Detailed Information
On selection of the Trail ID displayed in the Summary view, the detailed information of the changes will be displayed. Information displayed in this screen will be   
- Trail ID  
- Event Type  
- Modified Attribute 
- Modified At  
- Modified by Channel  
- Previous value  
- Current Value.   

<h3 id="soft-deletion">Soft Deletion</h3>
Soft deletion is a widely used pattern applied for business applications. It allows to mark some records as deleted without actual erasure from the database. Effectively, It prevent a soft-deleted record from being selected, search upon, meanwhile all old records can still refer to it.  

<h3 id="merge-unmerge">Merge and Unmerge</h3>
The process of merging records from one data source or multiple data sources and eliminating duplicate records. Unmerging of the Party record, where multiple records have been incorrectly merged under a single Party. 

#### Merging: 
While merging of two or more records into a single record, the parent record needs to be identified and the formation/attribute to be stored in the parent record of the Party needs to be mapped individually. The value to be retained in the parent record can be either at the individual record level or at attribute level.  

#### Un-merging: 
Un-merging of the single records into multiple records will be split with the value retained in one of the split records and the information in the second record needs to be updated separately either for all the attributes or for certain attribute 

<h3 id="advanced-search">Advanced Search</h3>
Non-Deterministic Search of the Party record will be executed through this functionality. List of records provided based on the input provided to the search functionality and actions like filtering, grouping and sorting can be performed for further analysis or finding the exact match. 

Search of customer records will be executed using the below functions and parameters mentioned: 

#### Fuzzy Search: 
Fuzzy search is a technique of searching information that is similar to the input or match a pattern approximately. Implementing this logic can be on text fields  
- First Name 
- Last Name 

#### Universal Search: 
Input provided to this search functionality will verify the data across the entire set of Party parameters and find an exact match. 

<h3 id="pii-data">PII Data</h3>
Personally Identifiable Information (PII) is any type of data that can be used to identify someone, from their name and address to their phone number, passport information, and social security numbers. 

Below are the attributes considered as PII in the CIF module


- Full Name
-  First Name
- Middle Name
- Last Name
- Mobile Number
- National ID
- Tax ID
- Address Line 1
- Address Line 2
- Address Line 3


#### Encryption and Decryption
Encryption is the process of using mathematical algorithms to obscure the meaning of a piece of information so that only authorized parties can decipher it. It is used to protect our data (including texts, conversations ad voice), be it sitting on a computer or it being transmitted over the Internet. Encryption technologies are one of the essential elements of any secure computing environment.

Symmetric encryption algorithms: Symmetric algorithms use the same key for encryption and decryption. These algorithms, can either operate in block mode (which works on fixed-size blocks of data) or stream mode (which works on bits or bytes of data). They are commonly used for applications like data encryption, file encryption and encrypting transmitted data in communication networks (like TLS, emails, instant messages, etc.).  

Asymmetric (or public key) encryption algorithms: Unlike symmetric algorithms, which use the same key for both encryption and decryption operations, asymmetric algorithms use two separate keys for these two operations. These algorithms are used for computing digital signatures and key establishment protocols.
