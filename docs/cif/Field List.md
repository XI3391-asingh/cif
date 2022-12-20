
## Overview

This section gives you the information on the list of attributes catered in the CIF module along with mapping to a logical entity against each attribute and sample value for enum fields and description of each attribute.

|	Entity	|	Field Name |	Sample Values	|	Descritpion	|
|---------|------------|---------------|--------------|
|Config |Type||Encryption, Universal Search Field|
|Config |Config Data||key: EMAIL, values:party.primaryEmail|
|Party |Identifier (CIF) ||CIF to identify the Party Record|
|Party |Type|Prospect, Customer-Retail, Customer-HNI, Corporate|Party Type|
|Party |Salutation|Mr, Mrs. Miss, Dr|Salutation of Party|
|Party |Full Name||Full Name of the party|
|Party |First Name||First Name of the party|
|Party |Middle Name||MIddle Name of the party|
|Party |Last Name||Last Name of the party|
|Party |Mothers Maiden Name||Maiden Name of mother|
|Party |Nick Name||Nick Name of Party [Card embossed name]|
|Party |Gender|Male, Female, Transgender, Do not want to disclose|Gender of Party|
|Party |Date of Birth||Date of Birth of Party|
|Party |Place of Birth||Place of Birth of Party|
|Party |Country of Birth Code|List of Country Codes|Country of Birth of Party|
|Party |Primary Mobile Number||Primary/Registered mobile number of Party|
|Party |Primary Email||Primary/Registered email address of Party|
|Party |Marital Status|Married, Unmarried, Divorced, Widowed|Marital Status of the Party|
|Party |Nationality|VI,IN|Nationality of the Party|
|Party |Nationality Id Type Code||Nationality Type of the Party|
|Party |Education Type Code||Type of Education of the Party|
|Party |SourceSystem||
|Party |Is Staff||Flag to identify Staff Party|
|Party |StaffCode||Staff Code of the Party|
|Party |CompanyCode||Company the Party is working for|
|Party |GroupCode|Group of Family, Corporate|Grouping of Party with a Code|
|Party |PortfolioCode (SalesPersonCode)||Sales/ RM Employee Code|
|Party |AML Risk |Low, Medium, High|AML Risk Category|
|Party |AML Risk Eval Date||Latest Date of Risk evaluated for Party|
|Party |REFERRAL CODE||Referral Code sourced the Party record|
|Party |PROMO CODE||Promo Code used while onboarding of Party|
|Party |Segment Code||Values Mass, Premium, Signature, Insignia|
|Party |Status|Active, Inactive, Blocked|Status of the Party|
|Party |WillFul Defaulter Date||Date of Last Willfully defaulted by Party|
|Occupation Details|Profession|Salaried, Self Employed, Goverment Service|Profession of the Party|
|Occupation Details |TAXID||Tax Identification Number of Party|
|Occupation Details |Date of Incorporation||Date of Incorporation of Party (Corporate)|
|Occupation Details|Profession Code|||
|Occupation Details|Profession Type|Full-time, Part-time|Type of Profession of the Party|
|Occupation Details|Industry Type / Occupation|Banking, IT, Goverment Service|Type of Industry Party is working in|
|Occupation Details|Company Type Code|||
|Occupation Details|Annual Income||Annual Income of the Party|
|Occupation Details|Annual Turnover||Annual Turnover of the Party (Corporate)|
|Party Flag|Is Deceased ||Flag to identify if the Party is deceased|
|Party Flag|Is Solvency ||Flag to identify the Party as insolvent|
|Party Flag|NPA ||Flag to identify if Party is a NPA(Non Performing Asset)|
|Party Flag|WillFul Defaulter Flag||Flag to identify if the Party is a Willful defaulter|
|Party Flag|IsLoanOverDue||Flag to Identify if any Loan is overdue for Party|
|Party Flag|Suit Filed Flag||Flag to identify if an Suit is filed against the Party|
|Party Flag|Is Politically Exposed Flag||Flag to identify if the Party is PEP (Politically exposed)|
|Party Flag|FATCA Applicable||Flag to identify if FATCA is applicable for the Party|
|Party Flag|Is Email Statement Reg||Flag to identify if the Party has registered to get Statements on Email|
|Party Flag|Is Under WatchList||Flag to identify if the Party is added to Watchlist|
||||
|Address Information|Id||ID of the address record|
|Address Information|Party Id||CIF to identify the Party Record|
|Address Information|Address Type|Permanent_Address, Correspondence_Address, Office_Address, Temporary_Address|Type of Address of the Party|
|Address Information|Is Default Flag||Default Address of the Party (For All Communications…TBD)|
|Address Information|Address Line 1||Address Line 1 the Party is residing at|
|Address Information|Address Line 2||Address Line 2 the Party is residing at|
|Address Information|Address Line 3||Address Line 3 the Party is residing at|
|Address Information|Ward||Area/Ward the Party is residing at|
|Address Information|District||District the Party is residing at|
|Address Information|City Name||City the Party is residing at|
|Address Information|City ZIP Code||Zipcode of the city the Party is residing at|
|Address Information|Country Code||Country the Party is residing at|
|Address Information|Document ID||Reference ID of the document provided as Address proof|
|Address Information|Country |||
||||
|Guardias |Guardian Relation||Type of Relation with the Guardian|
|Guardian|First Name||First Name of the Guardian Party|
|Guardian|Middle Name||Middle Name of the Guardian Party|
|Guardian|Last Name||Last Name of the Guardian Party|
|Guardian|Address Line 1||Address Line 1 the Guardian Party is residing at|
|Guardian|Address Line 2||Address Line 2 the Guardian Party is residing at|
|Guardian|Address Line 3||Address Line 3 the Guardian Party is residing at|
|Guardian|Ward||Area/Ward the Guardian Party is residing at|
|Guardian|District||District the Guardian Party is residing at|
|Guardian|City || City the Guardian Party is residing at|
||||
|Contact Details|ContactType|Mobile, Email, FaceBookId, Twitter Handle|Contact Details Type|
|Contact Details|ContactValue||Value associated with contact channel (Number, username, handle)|
|Contact Details|ISD code||ISD code of the contact details|
|Contact Details|IsPrimary||Flag to identify if the record is primary contact channel|
|Contact Details|IsVerified||Flag to identify if the record is a verified contact details|
|Contact Details|VerifiedMode||Mode of verification of the contact channel|
|Contact Details|LastVerifiedDate||Last verified date of contact channel|
|Contact Details|DND FLAG||Flag to identify if DND (Do not Disturb) is set by the party for the details|
||||
|Relation |Secondary Party Id||CIF to identify the Parent Party Record in Relation|
|Relation |RELATION|Father, Mother, Brother, Sister|Relationship between the Parent and Child party record|
|Relation |INV RELATION|Father, Mother, Brother, Sister|Inverse Relationship between the Parent and Child party record|
||||
|Assets |ASSET TYPE||Asset or Collaterals type|
|Assets |ASSET NAME||Asset Name|
|Assets |ASSEST POTENTIAL VALUE||Value of the Asset|
|Assets |IS MORTGAGED||Flag to identify if the Asset is mortgaged|
||||
|Document |Doc Type||Document type|
|Document |Doc No (PII)||Document number|
|Document |Doc No Masked||Document number in masked format|
|Document |Doc No Token||Document number token |
|Document |Issuing Date||Date of Issuance of the document|
|Document |Expiry Date||Expiry date of the document|
|Document |Issuing Place||Place of Issuance of the document|
|Document |Issuing Country||Country of Issuance of the document|
|Document |POI Flag||Flag to identy if the document os POA (Proof of Identity)|
|Document |POA Flag||Flag to identy if the document os POA (Proof of Address)|
|Document |DMS Ref Id||Document Reference ID from the DMS system|
|Document |Doc Verification Status|INPROCESS, APPROVED, REJECTED|Flag to identify the Document verification status|
|Document |Additional Meta Data |||
||||
|Fatca Details|Place Of Incorp||Place of Incorporation|
|Fatca Details|Country of Incorp||Country of Incorporation|
|Fatca Details|Country Of Residence||Country of residence|
|Fatca Details|Incorp No|||
|Fatca Details|Board Rel No|||
|Fatca Details|Report BL No|||
|Fatca Details|Original Report BL No|||
|Fatca Details|Fatca TaxId||Tax Identification Number of the Party|
|Fatca Details|Document Refrence Id|||
||||
|Risks|RISK TYPE|CIBIL_CHECK, AML_CHECK, KYC|Type of Risk|
|Risks|RISK SCORE||Score mapped again the risk|
|Risks|EVAL DATE||Risk evaluated date|
|Risks|VALID UNTIL||Risk score validity date|
||||
|Memos|MEMO TYPE (M)||Type of Memo|
|Memos|SEVERITY||Severity of the Memo|
|Memos|VALID FROM|||
|Memos|VALID UNTIL|||
||||
|Xref|Xref ID||ID mapped for the Cross reference record|
|Xref|System Code|||
||||

There is a list of general attributes mapped across all the entities  
|	Entity	|	Field Name |	Sample Values	|	Descritpion	|
|---------|------------|---------------|--------------|
|General|Is Deleted||Flag to identify if the Party is Soft Deleted|
|General|Created At||Datetime when the Party record got created|
|General|Created By||Details of the user who created the record|
|General|Modified At||Datetime when the Party record got last updated|
|General|Modified By||User details who last modified the Party record|
|General|CreatedBy Channel|Mobile App|Channel through which the Party record got created|
|General|LastModifiedBy Channel|Mobile App, Contact Center|Channel through which the Party record got last modified|
