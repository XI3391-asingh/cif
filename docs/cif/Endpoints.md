
## Create Config
<a id="opIdcreateConfig"></a>

> Code samples

`POST /admin`

>Body Parameters

```json
{

"type": "UNIVERSALSEARCHFIELDS",

"configData": [

	{

	"key": "EMAIL",

	"values": [

		"party.primaryEmail"

		]

	},

	{

	"key": "TEXT",

	"values": [

		"party.firstName",

		"party.lastName",

		"party.lastName",

		"party.fullName"

		]

	},

	{

	"key": "NUMBER",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "DATE",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "SPECIALCHAR",

	"values": [

		"party.dateOfBirth"

		]

	}

]

}
```
<h3 id="create-config-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[Config](#schemaconfig)|true|none|

> Example responses

> 201 Response

```json
{

"meta": {

	"requestID": "b8b25dfa-0e8a-4823-b6f5-7cdbc1962615"

},

"status": {

	"code": "CONFIG_UPDATED",

	"message": "Config record persist success"

},

"data": {

	"type": "UNIVERSALSEARCHFIELDS",

	"configData": [

	{

	"key": "EMAIL",

	"values": [

		"party.primaryEmail"

		]

	},

	{

	"key": "TEXT",

	"values": [

		"party.firstName",

		"party.lastName",

		"party.lastName",

		"party.fullName"

		]

	},

	{

	"key": "NUMBER",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "DATE",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "SPECIALCHAR",

	"values": [

		"party.dateOfBirth"

		]

	}

	]

}

}
```
<h3 id="create-party-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201/ 200|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Created|[ConfigResponse](#schemaconfigresponse)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid request body|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Config by type

<a id="opIdfetchAdminDetailByType"></a>

> Code samples

`GET /admin/{type}/`


<h3 id="finds-config-data-by-type">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|type|query|string|true|Type values that need to be considered for filter|

> Example responses

> 200 Response

```json
{

"meta": {

	"requestID": "f5c121d9-4b45-4945-9c42-7551f02af23a"

},

"status": {

	"code": "CONFIG_FETCH",

	"message": "Config record found!"

},

"data": {

	"type": "UNIVERSALSEARCHFIELDS",

	"configData": [

	{

	"key": "EMAIL",

	"values": [

		"party.primaryEmail"

		]

	},

	{

	"key": "TEXT",

	"values": [

		"party.firstName",

		"party.lastName",

		"party.lastName",

		"party.fullName"

		]

	},

	{

	"key": "NUMBER",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "DATE",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "SPECIALCHAR",

	"values": [

	"party.dateOfBirth"

		]

	 }

	]

}

}
```
<h3 id="finds-config-by-type">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid type value|None|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Error occurred while fetching config information|None|



## Open API Specification for Party v1.0.0

Scroll down for code samples, example requests and responses.

## Create Party

<a id="opIdcreateParty"></a>

> Code samples

`POST /party`

> Body parameter

```json
{
  "party": {
    "partyIdentifier": "qn37iDGkbWqf18CgP1YbICtoHBOURaT0VAQq",
    "type": "CUSTOMER",
    "salutationCode": "01",
    "fullName": "Joi Bristo Regino",
    "firstName": "Joi",
    "middleName": "Bristo",
    "lastName": "Regino",
    "mothersMaidenName": "Mia",
    "nickName": "Jo",
    "gender": "MALE",
    "dateOfBirth": "30-11-1997",
    "placeOfBirth": "Da Nang",
    "primaryMobileNumber": "9088563701",
    "primaryEmail": "joibristo434@gmail.com",
    "maritalStatus": "SINGLE",
    "status": "ACTIVE",
    "nationalIdTypeCode": "01",
    "nationalId": "80033848",
    "referralCode": "RC01",
    "promoCode": "T01",
    "relationTypeCode": "01",
    "segmentCode": "01",
    "countryOfBirthCode": "01",
    "nationalityCode": "01",
    "educationTypeCode": "01",
    "isStaff": true,
    "staffCode": "S22",
    "groupCode": "G21",
    "portfolioCode": "PP12",
    "countryOfResidenceCode": "01",
    "residencyTypeCode": "01",
    "jobPositionTypeCode": "02",
    "amlRisk": "MEDIUM",
    "amlRiskEvalDate": "20-11-2003",
    "amlCheckStatus": false,
    "sourceSystem": "string",
    "createdBy": "SYSTEM",
    "createdByChannel": "Web"
  },
  "occupationDetail": {
    "professionCode": "01",
    "professionTypeCode": "01",
    "industryTypeCode": "11",
    "companyTypeCode": "CG09",
    "annualIncome": 91019,
    "annualTurnover": 877887,
    "taxId": "TX01",
    "dateOfInCorporation": "05-10-1980"
  },
  "address": [
    {
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01"
    }
  ],
  "contactDetails": [
    {
      "contactTypeCode": "01",
      "contactValue": "9918831234",
      "isdCode": "84",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "verified",
      "lastVerifiedDate": "30-08-1982",
      "isDnd": true
    }
  ],
  "assets": [
    {
      "assetTypeCode": "01",
      "assetName": "house",
      "potentialValue": 99999999,
      "isMortgaged": true
    }
  ],
  "memos": [
    {
      "memoTypeCode": "01",
      "severity": "S1",
      "score": 11,
      "validFrom": "03-11-1901",
      "validTo": "02-11-1915"
    }
  ],
  "risks": [
    {
      "riskTypeCode": "01",
      "riskScore": 11,
      "evaluationDate": "02-04-2090",
      "validUntil": "20-03-2098"
    }
  ],
  "fatcaDetails": [
    {
      "placeOfIncorporation": "Da Nang",
      "countryOfIncorporation": "Vietnam",
      "countryOfResidence": "Vietnam",
      "incorporationNumber": "01",
      "boardRelNumber": "BR01",
      "reportBlNumber": "R01",
      "originalReportBlNumber": "OR1",
      "fatcaTaxId": "01",
      "documentReferenceId": "11"
    }
  ],
  "guardians": [
    {
      "guardianFirstName": "Anh",
      "guardianMiddleName": "Van",
      "guardianLastName": "Duc",
      "guardianRelation": "Father",
      "guardianAddressLine1": "Da Nang",
      "guardianAddressLine2": "House 14",
      "guardianAddressLine3": "District 17",
      "guardianWardCode": "01",
      "guardianDistrictCode": "01",
      "guardianCityCode": "01"
    }
  ],
  "relations": [
    {
      "secondaryPartyId": "02",
      "partyRelationTypeCode": "01",
      "invRelation": "Y"
    }
  ],
  "documents": [
    {
      "documentTypeCode": "01",
      "documentNumber": "11",
      "documentNumberMasked": "01",
      "documentNumberToken": "02",
      "issuingDate": "14-05-1972",
      "expiryDate": "24-05-1979",
      "issuingPlace": "Dong Nai",
      "issuingCountryCode": "01",
      "issuingCountry": "vietnam",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "01",
      "verificationStatus": "verified",
      "additionalData": "Y"
    }
  ]
}
```

<h3 id="create-party-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[PartyRequestCmd](#schemapartyrequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "party": {
      "partyIdentifier": "123456789012",
      "type": "CUSTOMER",
      "salutationCode": "01",
      "salutation": "Mr.",
      "fullName": "Joi Bristo Regino",
      "firstName": "Joi",
      "middleName": "Bristo",
      "lastName": "Regino",
      "mothersMaidenName": "Mia",
      "nickName": "Jo",
      "gender": "MALE",
      "dateOfBirth": "30-11-1997",
      "placeOfBirth": "Da Nang",
      "primaryMobileNumber": "9088563701",
      "primaryEmail": "joibristo434@gmail.com",
      "maritalStatus": "SINGLE",
      "status": "ACTIVE",
      "nationalIdTypeCode": "01",
      "nationalIdType": "01",
      "nationalId": "80033848",
      "referralCode": "RC01",
      "promoCode": "T01",
      "relationTypeCode": "01",
      "relationType": "01",
      "segmentCode": "11",
      "segment": "SEGMENT",
      "countryOfBirthCode": "01",
      "countryOfBirth": "IND",
      "nationalityCode": "01",
      "nationality": "Indian",
      "educationTypeCode": "01",
      "educationType": "01",
      "isStaff": true,
      "staffCode": "S22",
      "groupCode": "G21",
      "portfolioCode": "PP12",
      "countryOfResidenceCode": "01",
      "countryOfResidence": "01",
      "residencyTypeCode": "01",
      "residencyType": "01",
      "jobPositionTypeCode": "02",
      "jobPosition": "MANAGER",
      "amlRisk": "MEDIUM",
      "amlRiskEvalDate": "20-11-2003",
      "amlCheckStatus": false,
      "createdAt": "20-11-2003",
      "createdBy": "SYSTEM",
      "updatedAt": "20-11-2003",
      "updatedBy": "SYSTEM",
      "createdByChannel": "20-11-2003",
      "updatedByChannel": null,
      "sourceSystem": "string",
      "isDeleted": true
    },
    "occupationDetail": {
      "professionCode": "01",
      "profession": "01",
      "professionTypeCode": "01",
      "professionType": "01",
      "industryTypeCode": "01",
      "industryType": "industry1",
      "companyTypeCode": "01",
      "company": "Company1",
      "annualIncome": 91019,
      "annualTurnover": 877887,
      "taxId": "TX01",
      "dateOfInCorporation": "05-10-1980"
    },
    "partyFlag": {
      "isDeceased": true,
      "isSolvency": true,
      "isNpa": true,
      "isWillFullDefaulter": true,
      "willFullDefaulterDate": "26-12-4791",
      "isLoanOverDue": true,
      "isSuitFiled": true,
      "isPoliticallyExposed": true,
      "isFatcaApplicable": true,
      "isEmailStatementReg": true,
      "isUnderWatchList": true
    },
    "address": [
      {
        "addressTypeCode": "01",
        "isDefault": true,
        "addressLine1": "14/12 Ky Dong Street",
        "addressLine2": "Ward 3, District 17",
        "addressLine3": "HCMC",
        "wardCode": "01",
        "districtCode": "D01",
        "cityCode": "11",
        "cityZipCode": 11001,
        "countryCode": "84",
        "documentId": "01",
        "partyAddressId": 0,
        "ward": "string",
        "district": "string",
        "city": "string",
        "country": "string"
      }
    ],
    "contactDetails": [
      {
        "contactTypeCode": "01",
        "contactValue": "9918831234",
        "isdCode": "84",
        "isPrimary": true,
        "isVerified": true,
        "verifiedMode": "verified",
        "lastVerifiedDate": "30-08-1982",
        "isDnd": true,
        "partyContactDetailsId": 0,
        "contactType": "string"
      }
    ],
    "assets": [
      {
        "assetTypeCode": "01",
        "assetName": "house",
        "potentialValue": 99999999,
        "isMortgaged": true,
        "partyAssetId": 0,
        "assetType": "string"
      }
    ],
    "memos": [
      {
        "memoTypeCode": "01",
        "severity": "S1",
        "score": 11,
        "validFrom": "03-11-1901",
        "validTo": "02-11-1915",
        "partyMemoId": 0,
        "memoType": "string"
      }
    ],
    "risks": [
      {
        "riskTypeCode": "01",
        "riskScore": 11,
        "evaluationDate": "02-04-2090",
        "validUntil": "20-03-2098",
        "partyRiskId": 0,
        "riskType": "string"
      }
    ],
    "fatcaDetails": [
      {
        "placeOfIncorporation": "Da Nang",
        "countryOfIncorporation": "Vietnam",
        "countryOfResidence": "Vietnam",
        "incorporationNumber": "01",
        "boardRelNumber": "BR01",
        "reportBlNumber": "R01",
        "originalReportBlNumber": "OR1",
        "fatcaTaxId": "01",
        "documentReferenceId": "11",
        "partyFatcaDetailsId": 0
      }
    ],
    "guardians": [
      {
        "guardianFirstName": "Anh",
        "guardianMiddleName": "Van",
        "guardianLastName": "Duc",
        "guardianRelation": "Father",
        "guardianAddressLine1": "Da Nang",
        "guardianAddressLine2": "House 14",
        "guardianAddressLine3": "District 17",
        "guardianWardCode": "01",
        "guardianDistrictCode": "01",
        "guardianCityCode": "01",
        "partyGuardianId": 0,
        "guardianWard": "string",
        "guardianDistrict": "string",
        "guardianCity": "string"
      }
    ],
    "relations": [
      {
        "secondaryPartyId": "02",
        "partyRelationTypeCode": "01",
        "invRelation": "Y",
        "partyRelationId": 0,
        "partyRelationType": "string"
      }
    ],
    "xrefs": [
      {
        "systemCode": "01",
        "xrefId": "11",
        "partyXrefId": 0
      }
    ],
    "documents": [
      {
        "documentTypeCode": "01",
        "documentNumber": "11",
        "documentNumberMasked": "01",
        "documentNumberToken": "02",
        "issuingDate": "14-05-1972",
        "expiryDate": "24-05-1979",
        "issuingPlace": "Dong Nai",
        "issuingCountryCode": "01",
        "issuingCountry": "vietnam",
        "isPoi": true,
        "isPoa": true,
        "dmsReferenceId": "01",
        "verificationStatus": "verified",
        "additionalData": "Y",
        "partyDocumentId": 0,
        "documentType": "string"
      }
    ]
  }
}
```

<h3 id="create-party-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Created|[PartyResponseCmd](#schemapartyresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid request body|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Finds Customers by idtentifier

<a id="opIdfetchPartyDetailById"></a>

> Code samples

`GET /party`

Multiple identifier values can be provided with comma separated strings

<h3 id="finds-customers-by-ids-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|identifier|query|array[string]|true|Identifier values that need to be considered for filter|

> Example responses

> 200 Response

```json
[
  {
    "meta": {
      "requestID": "string"
    },
    "status": {
      "code": "string",
      "message": "string"
    },
    "data": {
      "party": {
        "partyIdentifier": "123456789012",
        "type": "CUSTOMER",
        "salutationCode": "01",
        "salutation": "Mr.",
        "fullName": "Joi Bristo Regino",
        "firstName": "Joi",
        "middleName": "Bristo",
        "lastName": "Regino",
        "mothersMaidenName": "Mia",
        "nickName": "Jo",
        "gender": "MALE",
        "dateOfBirth": "30-11-1997",
        "placeOfBirth": "Da Nang",
        "primaryMobileNumber": "9088563701",
        "primaryEmail": "joibristo434@gmail.com",
        "maritalStatus": "SINGLE",
        "status": "ACTIVE",
        "nationalIdTypeCode": "01",
        "nationalIdType": "01",
        "nationalId": "80033848",
        "referralCode": "RC01",
        "promoCode": "T01",
        "relationTypeCode": "01",
        "relationType": "01",
        "segmentCode": "11",
        "segment": "SEGMENT",
        "countryOfBirthCode": "01",
        "countryOfBirth": "IND",
        "nationalityCode": "01",
        "nationality": "Indian",
        "educationTypeCode": "01",
        "educationType": "01",
        "isStaff": true,
        "staffCode": "S22",
        "groupCode": "G21",
        "portfolioCode": "PP12",
        "countryOfResidenceCode": "01",
        "countryOfResidence": "01",
        "residencyTypeCode": "01",
        "residencyType": "01",
        "jobPositionTypeCode": "02",
        "jobPosition": "MANAGER",
        "amlRisk": "MEDIUM",
        "amlRiskEvalDate": "20-11-2003",
        "amlCheckStatus": false,
        "createdAt": "20-11-2003",
        "createdBy": "SYSTEM",
        "updatedAt": "20-11-2003",
        "updatedBy": "SYSTEM",
        "createdByChannel": "20-11-2003",
        "updatedByChannel": null,
        "sourceSystem": "string",
        "isDeleted": true
      },
      "occupationDetail": {
        "professionCode": "01",
        "profession": "01",
        "professionTypeCode": "01",
        "professionType": "01",
        "industryTypeCode": "01",
        "industryType": "industry1",
        "companyTypeCode": "01",
        "company": "Company1",
        "annualIncome": 91019,
        "annualTurnover": 877887,
        "taxId": "TX01",
        "dateOfInCorporation": "05-10-1980"
      },
      "partyFlag": {
        "isDeceased": true,
        "isSolvency": true,
        "isNpa": true,
        "isWillFullDefaulter": true,
        "willFullDefaulterDate": "30-01-1484",
        "isLoanOverDue": true,
        "isSuitFiled": true,
        "isPoliticallyExposed": true,
        "isFatcaApplicable": true,
        "isEmailStatementReg": true,
        "isUnderWatchList": true
      },
      "address": [
        {
          "addressTypeCode": "01",
          "isDefault": true,
          "addressLine1": "14/12 Ky Dong Street",
          "addressLine2": "Ward 3, District 17",
          "addressLine3": "HCMC",
          "wardCode": "01",
          "districtCode": "D01",
          "cityCode": "11",
          "cityZipCode": 11001,
          "countryCode": "84",
          "documentId": "01",
          "partyAddressId": 0,
          "ward": "string",
          "district": "string",
          "city": "string",
          "country": "string"
        }
      ],
      "contactDetails": [
        {
          "contactTypeCode": "01",
          "contactValue": "9918831234",
          "isdCode": "84",
          "isPrimary": true,
          "isVerified": true,
          "verifiedMode": "verified",
          "lastVerifiedDate": "30-08-1982",
          "isDnd": true,
          "partyContactDetailsId": 0,
          "contactType": "string"
        }
      ],
      "assets": [
        {
          "assetTypeCode": "01",
          "assetName": "house",
          "potentialValue": 99999999,
          "isMortgaged": true,
          "partyAssetId": 0,
          "assetType": "string"
        }
      ],
      "memos": [
        {
          "memoTypeCode": "01",
          "severity": "S1",
          "score": 11,
          "validFrom": "03-11-1901",
          "validTo": "02-11-1915",
          "partyMemoId": 0,
          "memoType": "string"
        }
      ],
      "risks": [
        {
          "riskTypeCode": "01",
          "riskScore": 11,
          "evaluationDate": "02-04-2090",
          "validUntil": "20-03-2098",
          "partyRiskId": 0,
          "riskType": "string"
        }
      ],
      "fatcaDetails": [
        {
          "placeOfIncorporation": "Da Nang",
          "countryOfIncorporation": "Vietnam",
          "countryOfResidence": "Vietnam",
          "incorporationNumber": "01",
          "boardRelNumber": "BR01",
          "reportBlNumber": "R01",
          "originalReportBlNumber": "OR1",
          "fatcaTaxId": "01",
          "documentReferenceId": "11",
          "partyFatcaDetailsId": 0
        }
      ],
      "guardians": [
        {
          "guardianFirstName": "Anh",
          "guardianMiddleName": "Van",
          "guardianLastName": "Duc",
          "guardianRelation": "Father",
          "guardianAddressLine1": "Da Nang",
          "guardianAddressLine2": "House 14",
          "guardianAddressLine3": "District 17",
          "guardianWardCode": "01",
          "guardianDistrictCode": "01",
          "guardianCityCode": "01",
          "partyGuardianId": 0,
          "guardianWard": "string",
          "guardianDistrict": "string",
          "guardianCity": "string"
        }
      ],
      "relations": [
        {
          "secondaryPartyId": "02",
          "partyRelationTypeCode": "01",
          "invRelation": "Y",
          "partyRelationId": 0,
          "partyRelationType": "string"
        }
      ],
      "xrefs": [
        {
          "systemCode": "01",
          "xrefId": "11",
          "partyXrefId": 0
        }
      ],
      "documents": [
        {
          "documentTypeCode": "01",
          "documentNumber": "11",
          "documentNumberMasked": "01",
          "documentNumberToken": "02",
          "issuingDate": "14-05-1972",
          "expiryDate": "24-05-1979",
          "issuingPlace": "Dong Nai",
          "issuingCountryCode": "01",
          "issuingCountry": "vietnam",
          "isPoi": true,
          "isPoa": true,
          "dmsReferenceId": "01",
          "verificationStatus": "verified",
          "additionalData": "Y",
          "partyDocumentId": 0,
          "documentType": "string"
        }
      ]
    }
  }
]
```

<h3 id="finds-customers-by-ids-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid identifier value|None|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Error occurred while fetching party information|None|

<h3 id="finds-customers-by-ids-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyResponseCmd](#schemapartyresponsecmd)]|false|none|none|
|»» party|[PartyCmd](#schemapartycmd)|false|none|none|
|»» partyIdentifier|string|false|none|none|
|»» type|string|true|none|none|
|»» salutationCode|string|true|none|none|
|»» salutation|string|false|none|none|
|»» fullName|string|true|none|none|
|»» firstName|string|true|none|none|
|»» middleName|string|false|none|none|
|»» lastName|string|false|none|none|
|»» mothersMaidenName|string|false|none|none|
|»» nickName|string|false|none|none|
|»» gender|string|true|none|none|
|»» dateOfBirth|string|true|none|DOB in DD-MM-YYYY format|
|»» placeOfBirth|string|false|none|none|
|»» primaryMobileNumber|string|true|none|none|
|»» primaryEmail|string(email)|true|none|none|
|»» maritalStatus|string|true|none|none|
|»» partyStatus|string|true|none|none|
|»» nationalIdTypeCode|string|false|none|none|
|»» nationalIdType|string|false|none|none|
|»» nationalId|string|false|none|none|
|»» referralCode|string|false|none|none|
|»» promoCode|string|false|none|none|
|»» relationTypeCode|string|false|none|none|
|»» relationType|string|false|none|none|
|»» segmentCode|string|false|none|none|
|»» segment|string|false|none|none|
|»» countryOfBirthCode|string|false|none|none|
|»» countryOfBirth|string|false|none|none|
|»» nationalityCode|string|false|none|none|
|»» nationality|string|false|none|none|
|»» educationTypeCode|string|false|none|none|
|»» educationType|string|false|none|none|
|»» isStaff|boolean|false|none|none|
|»» staffCode|string|false|none|none|
|»» groupCode|string|false|none|none|
|»» portfolioCode|string|false|none|none|
|»» countryOfResidenceCode|integer|false|none|none|
|»» countryResidence|string|false|none|none|
|»» residencyTypeCode|integer|false|none|none|
|»» residencyType|string|false|none|none|
|»» jobPosition|string|false|none|none|
|»» lastRiskEvalDate|string|false|none|Last riskeval date in DD-MM-YYYY format|
|»» createdByChannel|string|false|none|none|
|»» updatedByChannel|string|false|none|none|
|» occupationDetail|[OccupationDetailCmd](#schemaoccupationdetailcmd)|false|none|none|
|»» professionCode|string|false|none|none|
|»» professionTypeCode|string|false|none|none|
|»» industryTypeCode|string|false|none|none|
|»» companyTypeCode|string|false|none|none|
|»» annualIncome|integer|false|none|none|
|»» annualTurnover|integer|false|none|none|
|»» taxId|string|false|none|none|
|»» dateOfInCorporation|string|false|none|Date Of InCorporation in DD-MM-YYYY format|
|» partyFlag|object|false|none|none|
|»» amlCheckStatus|boolean|false|none|none|
|»» isDeceased|boolean|false|none|none|
|»» isSolvency|boolean|false|none|none|
|»» isNpa|boolean|false|none|none|
|»» isWillFullDefaulter|boolean|false|none|none|
|»» willFullDefaulterDate|string|false|none|Will Full Defaulter Date in DD-MM-YYYY format|
|»» isLoanOverDue|boolean|false|none|none|
|»» isSuitFiled|boolean|false|none|none|
|»» isPoliticallyExposed|boolean|false|none|none|
|»» isFatcaApplicable|boolean|false|none|none|
|»» isEmailStatementReg|boolean|false|none|none|
|»» isUnderWatchList|boolean|false|none|none|
|» address|[[PartyAddressCmd](#schemapartyaddresscmd)]|false|none|none|
|»» addressTypeCode|string|true|none|none|
|»» isDefault|boolean|false|none|none|
|»» addressLine1|string|true|none|none|
|»» addressLine2|string|true|none|none|
|»» addressLine3|string|false|none|none|
|»» wardCode|string|false|none|none|
|»» districtCode|string|false|none|none|
|»» cityCode|string|false|none|none|
|»» cityZipCode|integer|false|none|none|
|»» countryCode|string|false|none|none|
|»» documentId|string|false|none|none|
|»» partyAddressId|integer|false|none|none|
|» contactDetails|[[PartyContactDetailsCmd](#schemapartycontactdetailscmd)]|false|none|none|
|»» contactTypeCode|string|false|none|none|
|»» contactType|string|false|none|none|
|»» contactValue|string|false|none|none|
|»» isdCode|string|false|none|none|
|»» isPrimary|boolean|false|none|none|
|»» isVerified|boolean|false|none|none|
|»» verifiedMode|string|false|none|none|
|»» lastVerifiedDate|string|false|none|Last Verified Date in DD-MM-YYYY format|
|»» isDnd|boolean|false|none|none|
|»» partyContactDetailsId|integer|false|none|none|
|»» nationalIdMasked|string|false|none|none|
|»» nationalIdToken|string|false|none|none|
|»» percentage|number|false|none|none|
|» assets|[[PartyAssetsCmd](#schemapartyassetscmd)]|false|none|none|
|»» assetTypeCode|string|false|none|none|
|»» assetType|string|false|none|none|
|»» assetName|string|false|none|none|
|»» potentialValue|number|false|none|none|
|»» isMortgaged|boolean|false|none|none|
|»» partyAssetId|integer|false|none|none|
|» memos|[[PartyMemoCmd](#schemapartymemocmd)]|false|none|none|
|»» memoTypeCode|string|false|none|none|
|»» memoType|string|false|none|none|
|»» severity|string|false|none|none|
|»» score|number|false|none|none|
|»» validFrom|string|false|none|Valid From in DD-MM-YYYY format|
|»» validTo|string|false|none|Valid To in DD-MM-YYYY format|
|»» partyMemoId|integer|false|none|none|
|» risks|[[PartyRiskCmd](#schemapartyriskcmd)]|false|none|none|
|»» riskTypeCode|string|false|none|none|
|»» riskType|string|false|none|none|
|»» riskScore|number|false|none|none|
|»» evaluationDate|string|false|none|Evaluation Date in DD-MM-YYYY format|
|»» validUntil|string|false|none|Valid Until in DD-MM-YYYY format|
|»» partyRiskId|integer|false|none|none|
|» fatcaDetails|[[PartyFatcaDetailsCmd](#schemapartyfatcadetailscmd)]|false|none|none|
|»» placeOfIncorporation|string|false|none|none|
|»» countryOfIncorporation|string|false|none|none|
|»» countryOfResidence|string|false|none|none|
|»» incorporationNumber|string|false|none|none|
|»» boardRelNumber|string|false|none|none|
|»» reportBlNumber|string|false|none|none|
|»» originalReportBlNumber|string|false|none|none|
|»» fatcaTaxId|string|false|none|none|
|»» documentReferenceId|string|false|none|none|
|»» partyFatcaDetailsId|integer|false|none|none|
|» guardians|[[PartyGuardianCmd](#schemapartyguardiancmd)]|false|none|none|
|»» guardianFirstName|string|false|none|none|
|»» guardianMiddleName|string|false|none|none|
|»» guardianLastName|string|false|none|none|
|»» guardianAddressLine1|string|false|none|none|
|»» guardianAddressLine2|string|false|none|none|
|»» guardianAddressLine3|string|false|none|none|
|»» guardianWardCode|string|false|none|none|
|»» guardianWard|string|false|none|none|
|»» guardianDistrictCode|string|false|none|none|
|»» guardianDistrict|string|false|none|none|
|»» guardianCityCode|string|false|none|none|
|»» guardianCity|string|false|none|none|
|»» partyGuardianId|integer|false|none|none|
|» relations|[[PartyRelationCmd](#schemapartyrelationcmd)]|false|none|none|
|»» secondaryPartyId|string|false|none|none|
|»» partyRelationTypeCode|string|false|none|none|
|»» partyRelationType|string|false|none|none|
|»» invRelation|string|false|none|none|
|»» partyRelationId|integer|false|none|none|
|» xrefs|[[PartyXrefCmd](#schemapartyxrefcmd)]|false|none|none|
|»» systemCode|string|false|none|none|
|»» xrefId|string|false|none|none|
|»» partyXrefId|integer|false|none|none|
|»» relation|string|false|none|none|
|» documents|[[PartyDocumentCmd](#schemapartydocumentcmd)]|false|none|none|
|»» documentTypeCode|string|false|none|none|
|»» documentType|string|false|none|none|
|»» documentNumber|string|false|none|none|
|»» documentNumberMasked|string|false|none|none|
|»» documentNumberToken|string|false|none|none|
|»» issuingDate|string|false|none|Issuing Date in DD-MM-YYYY format|
|»» expiryDate|string|false|none|Expiry Date in DD-MM-YYYY format|
|»» issuingPlace|string|false|none|none|
|»» issuingCountryCode|string|false|none|none|
|»» issuingCountry|string|false|none|none|
|»» isPoi|boolean|false|none|none|
|»» isPoa|boolean|false|none|none|
|»» dmsReferenceId|string|false|none|none|
|»» verificationStatus|string|false|none|none|
|»» additionalData|string|false|none|none|
|»» partyDocumentId|integer|false|none|none|

<aside class="success">
This operation does not require authentication
</aside>

## Generic Update Party Record

<a id="opIdupdateParty"></a>

> Code samples

`PUT /party/update`

> Body parameter

```json
{
  "party": {
    "partyIdentifier": "123456789012",
    "type": "CUSTOMER",
    "salutationCode": "01",
    "fullName": "Joi Bristo Regino",
    "firstName": "Joi",
    "middleName": "Bristo",
    "lastName": "Regino",
    "mothersMaidenName": "Mia",
    "nickName": "Jo",
    "gender": "MALE",
    "dateOfBirth": "30-11-1997",
    "placeOfBirth": "Da Nang",
    "primaryMobileNumber": "9088563701",
    "primaryEmail": "joibristo434@gmail.com",
    "maritalStatus": "SINGLE",
    "status": "ACTIVE",
    "nationalIdTypeCode": "01",
    "nationalId": "80033848",
    "referralCode": "RC01",
    "promoCode": "T01",
    "relationTypeCode": "01",
    "segmentCode": "01",
    "countryOfBirthCode": "01",
    "nationalityCode": "01",
    "educationTypeCode": "01",
    "isStaff": true,
    "staffCode": "S22",
    "groupCode": "G21",
    "portfolioCode": "PP12",
    "countryOfResidenceCode": "01",
    "residencyTypeCode": "01",
    "jobPositionTypeCode": "02",
    "amlRisk": "MEDIUM",
    "amlRiskEvalDate": "20-11-2003",
    "amlCheckStatus": false,
    "sourceSystem": "string",
    "createdBy": "SYSTEM",
    "updatedByChannel": "Web",
    "updatedBy": "SYSTEM"
  },
  "occupationDetail": {
    "professionCode": "01",
    "professionTypeCode": "01",
    "industryTypeCode": "11",
    "companyTypeCode": "CG09",
    "annualIncome": 91019,
    "annualTurnover": 877887,
    "taxId": "TX01",
    "dateOfInCorporation": "05-10-1980"
  },
  "partyFlag": {
    "isDeceased": true,
    "isSolvency": true,
    "isNpa": true,
    "isWillFullDefaulter": true,
    "willFullDefaulterDate": "16-11-2317",
    "isLoanOverDue": true,
    "isSuitFiled": true,
    "isPoliticallyExposed": true,
    "isFatcaApplicable": true,
    "isEmailStatementReg": true,
    "isUnderWatchList": true
  },
  "address": [
    {
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01",
      "partyAddressId": 1
    }
  ],
  "contactDetails": [
    {
      "contactTypeCode": "01",
      "contactValue": "9918831234",
      "isdCode": "84",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "verified",
      "lastVerifiedDate": "30-08-1982",
      "isDnd": true,
      "partyContactDetailsId": 1
    }
  ],
  "assets": [
    {
      "assetTypeCode": "01",
      "assetName": "house",
      "potentialValue": 99999999,
      "isMortgaged": true,
      "partyAssetId": 1
    }
  ],
  "memos": [
    {
      "memoTypeCode": "01",
      "severity": "S1",
      "score": 11,
      "validFrom": "03-11-1901",
      "validTo": "02-11-1915",
      "partyMemoId": 1
    }
  ],
  "risks": [
    {
      "riskTypeCode": "01",
      "riskScore": 11,
      "evaluationDate": "02-04-2090",
      "validUntil": "20-03-2098",
      "partyRiskId": 1
    }
  ],
  "fatcaDetails": [
    {
      "placeOfIncorporation": "Da Nang",
      "countryOfIncorporation": "Vietnam",
      "countryOfResidence": "Vietnam",
      "incorporationNumber": "01",
      "boardRelNumber": "BR01",
      "reportBlNumber": "R01",
      "originalReportBlNumber": "OR1",
      "fatcaTaxId": "01",
      "documentReferenceId": "11",
      "partyFatcaDetailsId": 1
    }
  ],
  "guardians": [
    {
      "guardianFirstName": "Anh",
      "guardianMiddleName": "Van",
      "guardianLastName": "Duc",
      "guardianRelation": "Father",
      "guardianAddressLine1": "Da Nang",
      "guardianAddressLine2": "House 14",
      "guardianAddressLine3": "District 17",
      "guardianWardCode": "01",
      "guardianDistrictCode": "01",
      "guardianCityCode": "01",
      "partyGuardianId": 1
    }
  ],
  "relations": [
    {
      "secondaryPartyId": "02",
      "partyRelationTypeCode": "01",
      "invRelation": "Y",
      "partyRelationId": 1
    }
  ],
  "documents": [
    {
      "documentTypeCode": "01",
      "documentNumber": "11",
      "documentNumberMasked": "01",
      "documentNumberToken": "02",
      "issuingDate": "14-05-1972",
      "expiryDate": "24-05-1979",
      "issuingPlace": "Dong Nai",
      "issuingCountryCode": "01",
      "issuingCountry": "vietnam",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "01",
      "verificationStatus": "verified",
      "additionalData": "Y",
      "partyDocumentId": 1
    }
  ]
}
```

<h3 id="generic-update-party-record-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[PartyRequestCmd](#schemapartyrequestcmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="generic-update-party-record-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Dedupe check

<a id="dedupe"></a>

> Code samples

`POST /party/dedupe`

> Body parameter

```json
{
  "countryCode": "01",
  "fullName": "Joi Bristo Regino",
  "mobileNumber": "9400000290",
  "dateOfBirth": "30-11-1997",
  "nationalIdTypeCode": "01",
  "nationalId": "80033848",
  "nationalityCode": "01"
}
```

<h3 id="de-dupe-check-using-mobile-number-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[DedupeRequestCmd](#schemadeduperequestcmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "info": "string",
    "result": [
      {
        "party": {
          "partyIdentifier": "123456789012",
          "type": "CUSTOMER",
          "salutationCode": "01",
          "salutation": "Mr.",
          "fullName": "Joi Bristo Regino",
          "firstName": "Joi",
          "middleName": "Bristo",
          "lastName": "Regino",
          "mothersMaidenName": "Mia",
          "nickName": "Jo",
          "gender": "MALE",
          "dateOfBirth": "30-11-1997",
          "placeOfBirth": "Da Nang",
          "primaryMobileNumber": "9088563701",
          "primaryEmail": "joibristo434@gmail.com",
          "maritalStatus": "SINGLE",
          "status": "ACTIVE",
          "nationalIdTypeCode": "01",
          "nationalIdType": "01",
          "nationalId": "80033848",
          "referralCode": "RC01",
          "promoCode": "T01",
          "relationTypeCode": "01",
          "relationType": "01",
          "segmentCode": "11",
          "segment": "SEGMENT",
          "countryOfBirthCode": "01",
          "countryOfBirth": "IND",
          "nationalityCode": "01",
          "nationality": "Indian",
          "educationTypeCode": "01",
          "educationType": "01",
          "isStaff": true,
          "staffCode": "S22",
          "groupCode": "G21",
          "portfolioCode": "PP12",
          "countryOfResidenceCode": "01",
          "countryOfResidence": "01",
          "residencyTypeCode": "01",
          "residencyType": "01",
          "jobPositionTypeCode": "02",
          "jobPosition": "MANAGER",
          "amlRisk": "MEDIUM",
          "amlRiskEvalDate": "20-11-2003",
          "amlCheckStatus": false,
          "createdAt": "20-11-2003",
          "createdBy": "SYSTEM",
          "updatedAt": "20-11-2003",
          "updatedBy": "SYSTEM",
          "createdByChannel": "20-11-2003",
          "updatedByChannel": null,
          "sourceSystem": "string",
          "isDeleted": true
        },
        "occupationDetail": {
          "professionCode": "01",
          "profession": "01",
          "professionTypeCode": "01",
          "professionType": "01",
          "industryTypeCode": "01",
          "industryType": "industry1",
          "companyTypeCode": "01",
          "company": "Company1",
          "annualIncome": 91019,
          "annualTurnover": 877887,
          "taxId": "TX01",
          "dateOfInCorporation": "05-10-1980"
        },
        "partyFlag": {
          "isDeceased": true,
          "isSolvency": true,
          "isNpa": true,
          "isWillFullDefaulter": true,
          "willFullDefaulterDate": "06-01-0169",
          "isLoanOverDue": true,
          "isSuitFiled": true,
          "isPoliticallyExposed": true,
          "isFatcaApplicable": true,
          "isEmailStatementReg": true,
          "isUnderWatchList": true
        },
        "address": [
          {
            "addressTypeCode": "01",
            "isDefault": true,
            "addressLine1": "14/12 Ky Dong Street",
            "addressLine2": "Ward 3, District 17",
            "addressLine3": "HCMC",
            "wardCode": "01",
            "districtCode": "D01",
            "cityCode": "11",
            "cityZipCode": 11001,
            "countryCode": "84",
            "documentId": "01",
            "partyAddressId": 0,
            "ward": "string",
            "district": "string",
            "city": "string",
            "country": "string"
          }
        ],
        "contactDetails": [
          {
            "contactTypeCode": "01",
            "contactValue": "9918831234",
            "isdCode": "84",
            "isPrimary": true,
            "isVerified": true,
            "verifiedMode": "verified",
            "lastVerifiedDate": "30-08-1982",
            "isDnd": true,
            "partyContactDetailsId": 0,
            "contactType": "string"
          }
        ],
        "assets": [
          {
            "assetTypeCode": "01",
            "assetName": "house",
            "potentialValue": 99999999,
            "isMortgaged": true,
            "partyAssetId": 0,
            "assetType": "string"
          }
        ],
        "memos": [
          {
            "memoTypeCode": "01",
            "severity": "S1",
            "score": 11,
            "validFrom": "03-11-1901",
            "validTo": "02-11-1915",
            "partyMemoId": 0,
            "memoType": "string"
          }
        ],
        "risks": [
          {
            "riskTypeCode": "01",
            "riskScore": 11,
            "evaluationDate": "02-04-2090",
            "validUntil": "20-03-2098",
            "partyRiskId": 0,
            "riskType": "string"
          }
        ],
        "fatcaDetails": [
          {
            "placeOfIncorporation": "Da Nang",
            "countryOfIncorporation": "Vietnam",
            "countryOfResidence": "Vietnam",
            "incorporationNumber": "01",
            "boardRelNumber": "BR01",
            "reportBlNumber": "R01",
            "originalReportBlNumber": "OR1",
            "fatcaTaxId": "01",
            "documentReferenceId": "11",
            "partyFatcaDetailsId": 0
          }
        ],
        "guardians": [
          {
            "guardianFirstName": "Anh",
            "guardianMiddleName": "Van",
            "guardianLastName": "Duc",
            "guardianRelation": "Father",
            "guardianAddressLine1": "Da Nang",
            "guardianAddressLine2": "House 14",
            "guardianAddressLine3": "District 17",
            "guardianWardCode": "01",
            "guardianDistrictCode": "01",
            "guardianCityCode": "01",
            "partyGuardianId": 0,
            "guardianWard": "string",
            "guardianDistrict": "string",
            "guardianCity": "string"
          }
        ],
        "relations": [
          {
            "secondaryPartyId": "02",
            "partyRelationTypeCode": "01",
            "invRelation": "Y",
            "partyRelationId": 0,
            "partyRelationType": "string"
          }
        ],
        "xrefs": [
          {
            "systemCode": "01",
            "xrefId": "11",
            "partyXrefId": 0
          }
        ],
        "documents": [
          {
            "documentTypeCode": "01",
            "documentNumber": "11",
            "documentNumberMasked": "01",
            "documentNumberToken": "02",
            "issuingDate": "14-05-1972",
            "expiryDate": "24-05-1979",
            "issuingPlace": "Dong Nai",
            "issuingCountryCode": "01",
            "issuingCountry": "vietnam",
            "isPoi": true,
            "isPoa": true,
            "dmsReferenceId": "01",
            "verificationStatus": "verified",
            "additionalData": "Y",
            "partyDocumentId": 0,
            "documentType": "string"
          }
        ]
      }
    ]
  }
}
```

<h3 id="de-dupe-check-using-mobile-number-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Mobile Number Verification Complete|[DedupeResponseCmd](#schemadeduperesponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid request body|[BadRequestError](#schemabadrequesterror)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## search

<a id="opIdsearch"></a>

> Code samples

`POST /party/search`

> Body parameter

```json
{
  "partyIdentifier": "1234567890",
  "firstName": "Dan",
  "lastName": "Lang",
  "mobileNumber": "9899112276",
  "emailId": "dane1@gmail.com",
  "dateOfBirth": "01-01-1967"
}
```

<h3 id="search-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|limit|query|integer|false|The maximum number of items that may be returned for a single request. i.e pagesize|
|offset|query|integer|false|The starting point within the collection of resource results.|
|sortingOrder|query|string|true|The starting point within the collection of resource results.|
|isDelete|query|boolean|true|Flag to determine whether search should contain record marked as deleted record or not|
|body|body|[PartyDistinctiveSearchCmd](#schemapartydistinctivesearchcmd)|true|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|sortingOrder|asc|
|sortingOrder|desc|
|isDelete|true|
|isDelete|false|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "limit": 0,
    "offset": 0,
    "total": 0,
    "result": [
      {
        "partyIdentifier": "string",
        "fullName": "string",
        "firstName": "string",
        "middleName": "string",
        "lastName": "string",
        "dateOfBirth": "string",
        "mobileNumber": "string",
        "emailId": "string",
        "nationality": "string",
        "onBoardingDate": "string",
        "customerRiskCategory": "string"
      }
    ]
  }
}
```

<h3 id="search-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Search success|[PartySearchResponseCmd](#schemapartysearchresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Invalid request body|[BadRequestError](#schemabadrequesterror)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Status

<a id="opIdupdatePartyStatus"></a>

> Code samples

`PUT /party/update/status`

> Body parameter

```json
{
  "partyIdentifier": "1234567890",
  "status": "INACTIVE"
}
```

<h3 id="update-party-status-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[PartyContactUpdateCmd](#schemapartycontactupdatecmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-status-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Status Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Status Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Email

<a id="opIdupdatePartyEmail"></a>

> Code samples

`PUT /party/update/email`

> Body parameter

```json
{
  "partyIdentifier": "1234567890",
  "primaryEmail": "test@gamil.com"
}
```

<h3 id="update-party-email-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[PartyContactUpdateCmd](#schemapartycontactupdatecmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-email-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Email Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Email Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Mobile Number

<a id="opIdupdatePartyMobileNumber"></a>

> Code samples

`PUT /party/update/mobileNumber`

> Body parameter

```json
{
  "partyIdentifier": "1234567890",
  "primaryMobileNumber": "9400000290"
}
```

<h3 id="update-party-mobile-number-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[PartyContactUpdateCmd](#schemapartycontactupdatecmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-mobile-number-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Mobile Number Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Mobile Number Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Record For Soft Delete

<a id="opIdupdatePartyRecordForSoftDelete"></a>

> Code samples

`PUT /party/soft-delete/`

> Body parameter

```json
{
  "partyIdentifier": "1234567890"
}
```

<h3 id="update-party-record-for-soft-delete-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|object|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-record-for-soft-delete-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Document Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Document Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-address">Address</h1>

## Create Party Address

<a id="opIdcreatePartyAddress"></a>

> Code samples

`POST /party/{partyIdentifier}/address`

> Body parameter

```json
[
  {
    "addressTypeCode": "01",
    "isDefault": true,
    "addressLine1": "14/12 Ky Dong Street",
    "addressLine2": "Ward 3, District 17",
    "addressLine3": "HCMC",
    "wardCode": "01",
    "districtCode": "D01",
    "cityCode": "11",
    "cityZipCode": 11001,
    "countryCode": "84",
    "documentId": "01"
  }
]
```

<h3 id="create-party-address-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyAddressRequestCmd](#schemapartyaddressrequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01",
      "partyAddressId": 0,
      "ward": "string",
      "district": "string",
      "city": "string",
      "country": "string"
    }
  ]
}
```

<h3 id="create-party-address-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Address Create Success|[PartyAddressResponseCmd](#schemapartyaddressresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Address Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party All Addresses

<a id="opIdfetchAllAddress"></a>

> Code samples

`GET /party/{partyIdentifier}/address/`

<h3 id="fetch-party-addresses-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01",
      "partyAddressId": 0,
      "ward": "string",
      "district": "string",
      "city": "string",
      "country": "string"
    }
  ]
}

```

<h3 id="fetch-party-addresses-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyAddressResponseCmd](#schemapartyaddressresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party Address

<a id="opIdfetchAddress"></a>

> Code samples

`GET /party/{partyIdentifier}/address/{partyAddressId}`

<h3 id="fetch-party-address-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyAddressId|path|integer(int64)|true|Party Address Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "addressTypeCode": "01",
    "isDefault": true,
    "addressLine1": "14/12 Ky Dong Street",
    "addressLine2": "Ward 3, District 17",
    "addressLine3": "HCMC",
    "wardCode": "01",
    "districtCode": "D01",
    "cityCode": "11",
    "cityZipCode": 11001,
    "countryCode": "84",
    "documentId": "01",
    "partyAddressId": 0,
    "ward": "string",
    "district": "string",
    "city": "string",
    "country": "string"
  }
}
```

<h3 id="fetch-party-address-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyAddressCmd](#schemapartyaddresscmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Address

<a id="opIdupdatePartyAddress"></a>

> Code samples

`PUT /party/{partyIdentifier}/address/{partyAddressId}/`

> Body parameter

```json
{
  "addressTypeCode": "01",
  "isDefault": true,
  "addressLine1": "14/12 Ky Dong Street",
  "addressLine2": "Ward 3, District 17",
  "addressLine3": "HCMC",
  "wardCode": "01",
  "districtCode": "D01",
  "cityCode": "11",
  "cityZipCode": 11001,
  "countryCode": "84",
  "documentId": "01",
  "partyAddressId": 1
}
```

<h3 id="update-party-address-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyAddressId|path|integer(int64)|true|Party Address Id|
|body|body|[PartyAddressCmd](#schemapartyaddresscmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-address-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Address Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Address Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Delete Party Address

<a id="opIddeletePartyAddress"></a>

> Code samples

`DELETE /party/{partyIdentifier}/address/{partyAddressId}`

<h3 id="delete-party-address-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyAddressId|path|integer(int64)|true|Party AddressId|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="delete-party-address-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-contacts">Contacts</h1>

## Create Contacts

<a id="opIdcreateContacts"></a>

> Code samples

`POST /party/{partyIdentifier}/contacts`

> Body parameter

```json
[
  {
    "contactTypeCode": "01",
    "contactValue": "9918831234",
    "isdCode": "84",
    "isPrimary": true,
    "isVerified": true,
    "verifiedMode": "verified",
    "lastVerifiedDate": "30-08-1982",
    "isDnd": true
  }
]
```

<h3 id="create-contacts-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyContactsRequestCmd](#schemapartycontactsrequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "contactTypeCode": "01",
      "contactValue": "9918831234",
      "isdCode": "84",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "verified",
      "lastVerifiedDate": "30-08-1982",
      "isDnd": true,
      "partyContactDetailsId": 0,
      "contactType": "string"
    }
  ]
}
```

<h3 id="create-contacts-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Contact Create Success|[PartyContactsResponseCmd](#schemapartycontactsresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Contact Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Contacts

<a id="opIdupdateContacts"></a>

> Code samples

`PUT /party/{partyIdentifier}/contacts/{partyContactDetailsId}/`

> Body parameter

```json
{
  "contactTypeCode": "01",
  "contactValue": "9918831234",
  "isdCode": "84",
  "isPrimary": true,
  "isVerified": true,
  "verifiedMode": "verified",
  "lastVerifiedDate": "30-08-1982",
  "isDnd": true,
  "partyContactDetailsId": 1
}
```

<h3 id="update-contacts-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyContactDetailsId|path|integer(int64)|true|Party Contact Details Id|
|body|body|[PartyContactDetailsCmd](#schemapartycontactdetailscmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-contacts-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Contact Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Contact Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Contact

<a id="opIdfetchContact"></a>

> Code samples

`GET /party/{partyIdentifier}/contacts/{partyContactDetailsId}`

<h3 id="fetch-contact-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyContactDetailsId|path|integer(int64)|true|Party Contact Details Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "contactTypeCode": "01",
    "contactValue": "9918831234",
    "isdCode": "84",
    "isPrimary": true,
    "isVerified": true,
    "verifiedMode": "verified",
    "lastVerifiedDate": "30-08-1982",
    "isDnd": true,
    "partyContactDetailsId": 0,
    "contactType": "string"
  }
}
```

<h3 id="fetch-contact-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyContactsResponseCmd](#schemapartycontactsresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch All Contacts

<a id="opIdfetchAllContacts"></a>

> Code samples

`GET /party/{partyIdentifier}/contacts/`

<h3 id="fetch-all-contacts-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "contactTypeCode": "01",
      "contactValue": "9918831234",
      "isdCode": "84",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "verified",
      "lastVerifiedDate": "30-08-1982",
      "isDnd": true,
      "partyContactDetailsId": 0,
      "contactType": "string"
    }
  ]
}
```

<h3 id="fetch-all-contacts-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyContactsResponseCmd](#schemapartycontactsresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Delete Party Contact

<a id="opIddeletePartyContact"></a>

> Code samples

`DELETE /party/{partyIdentifier}/contact/{partyContactId}`

<h3 id="delete-party-contact-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyContactId|path|integer(int64)|true|Party ContactId|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="delete-party-contact-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-risks">Risks</h1>

## Create Party Risks

<a id="opIdcreatePartyRisks"></a>

> Code samples

`POST /party/{partyIdentifier}/risks`

> Body parameter

```json
[
  {
    "riskTypeCode": "01",
    "riskScore": 11,
    "evaluationDate": "02-04-2090",
    "validUntil": "20-03-2098"
  }
]
```

<h3 id="create-party-risks-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyRisksRequestCmd](#schemapartyrisksrequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "riskTypeCode": "01",
      "riskScore": 11,
      "evaluationDate": "02-04-2090",
      "validUntil": "20-03-2098",
      "partyRiskId": 0,
      "riskType": "string"
    }
  ]
}
```

<h3 id="create-party-risks-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Risks Create Success|[PartyRisksResponseCmd](#schemapartyrisksresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Risks Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Risks

<a id="opIdupdatePartyRisks"></a>

> Code samples

`PUT /party/{partyIdentifier}/risks/{partyRiskId}/`

> Body parameter

```json
{
  "riskTypeCode": "01",
  "riskScore": 11,
  "evaluationDate": "02-04-2090",
  "validUntil": "20-03-2098",
  "partyRiskId": 1
}
```

<h3 id="update-party-risks-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyRiskId|path|integer(int64)|true|Party Risks Id|
|body|body|[PartyRiskCmd](#schemapartyriskcmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-risks-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Risks Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Risks Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party Risks

<a id="opIdfetchPartyRisks"></a>

> Code samples

`GET /party/{partyIdentifier}/risks/{partyRiskId}`

<h3 id="fetch-party-risks-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyRiskId|path|integer(int64)|true|Party Risks Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "riskTypeCode": "01",
    "riskScore": 11,
    "evaluationDate": "02-04-2090",
    "validUntil": "20-03-2098",
    "partyRiskId": 0,
    "riskType": "string"
  }
}
```

<h3 id="fetch-party-risks-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyRisksResponseCmd](#schemapartyrisksresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch All Party Risks

<a id="opIdfetchAllPartyRisks"></a>

> Code samples

`GET /party/{partyIdentifier}/risks/`

<h3 id="fetch-all-party-risks-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "riskTypeCode": "01",
      "riskScore": 11,
      "evaluationDate": "02-04-2090",
      "validUntil": "20-03-2098",
      "partyRiskId": 0,
      "riskType": "string"
    }
  ]
}
```

<h3 id="fetch-all-party-risks-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyRisksResponseCmd](#schemapartyrisksresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-document">Document</h1>

## Create Party Document

<a id="opIdcreatePartyDocument"></a>

> Code samples

`POST /party/{partyIdentifier}/documents`

> Body parameter

```json
[
  {
    "documentTypeCode": "01",
    "documentNumber": "11",
    "documentNumberMasked": "01",
    "documentNumberToken": "02",
    "issuingDate": "14-05-1972",
    "expiryDate": "24-05-1979",
    "issuingPlace": "Dong Nai",
    "issuingCountryCode": "01",
    "issuingCountry": "vietnam",
    "isPoi": true,
    "isPoa": true,
    "dmsReferenceId": "01",
    "verificationStatus": "verified",
    "additionalData": "Y"
  }
]
```

<h3 id="create-party-document-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyDocumentsRequestCmd](#schemapartydocumentsrequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "documentTypeCode": "01",
      "documentNumber": "11",
      "documentNumberMasked": "01",
      "documentNumberToken": "02",
      "issuingDate": "14-05-1972",
      "expiryDate": "24-05-1979",
      "issuingPlace": "Dong Nai",
      "issuingCountryCode": "01",
      "issuingCountry": "vietnam",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "01",
      "verificationStatus": "verified",
      "additionalData": "Y",
      "partyDocumentId": 0,
      "documentType": "string"
    }
  ]
}
```

<h3 id="create-party-document-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Document Create Success|[PartyDocumentsResponseCmd](#schemapartydocumentsresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Document Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party All Documents

<a id="opIdfetchAllDocuments"></a>

> Code samples

`GET /party/{partyIdentifier}/documents/`

<h3 id="fetch-party-documents-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "documentTypeCode": "01",
      "documentNumber": "11",
      "documentNumberMasked": "01",
      "documentNumberToken": "02",
      "issuingDate": "14-05-1972",
      "expiryDate": "24-05-1979",
      "issuingPlace": "Dong Nai",
      "issuingCountryCode": "01",
      "issuingCountry": "vietnam",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "01",
      "verificationStatus": "verified",
      "additionalData": "Y",
      "partyDocumentId": 0,
      "documentType": "string"
    }
  ]
}
```

<h3 id="fetch-party-documents-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyDocumentsResponseCmd](#schemapartydocumentsresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party Document

<a id="opIdfetchDocument"></a>

> Code samples

`GET /party/{partyIdentifier}/documents/{partyDocumentId}`

<h3 id="fetch-party-document-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyDocumentsId|path|integer(int64)|true|Party Document Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "documentTypeCode": "01",
    "documentNumber": "11",
    "documentNumberMasked": "01",
    "documentNumberToken": "02",
    "issuingDate": "14-05-1972",
    "expiryDate": "24-05-1979",
    "issuingPlace": "Dong Nai",
    "issuingCountryCode": "01",
    "issuingCountry": "vietnam",
    "isPoi": true,
    "isPoa": true,
    "dmsReferenceId": "01",
    "verificationStatus": "verified",
    "additionalData": "Y",
    "partyDocumentId": 0,
    "documentType": "string"
  }
}
```

<h3 id="fetch-party-document-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyDocumentCmd](#schemapartydocumentcmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Documents

<a id="opIdupdatePartyDocuments"></a>

> Code samples

`PUT /party/{partyIdentifier}/documents/{partyDocumentId}/`

> Body parameter

```json
{
  "documentTypeCode": "01",
  "documentNumber": "11",
  "documentNumberMasked": "01",
  "documentNumberToken": "02",
  "issuingDate": "14-05-1972",
  "expiryDate": "24-05-1979",
  "issuingPlace": "Dong Nai",
  "issuingCountryCode": "01",
  "issuingCountry": "vietnam",
  "isPoi": true,
  "isPoa": true,
  "dmsReferenceId": "01",
  "verificationStatus": "verified",
  "additionalData": "Y",
  "partyDocumentId": 1
}
```

<h3 id="update-party-documents-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyDocumentsId|path|integer(int64)|true|Party Document Id|
|body|body|[PartyDocumentCmd](#schemapartydocumentcmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-documents-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Document Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Document Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Delete Party Document

<a id="opIddeletePartyDocument"></a>

> Code samples

`DELETE /party/{partyIdentifier}/document/{partyDocumentId}`

<h3 id="delete-party-document-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyDocumentId|path|integer(int64)|true|Party DocumentId|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="delete-party-document-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-memos">Memos</h1>

## Create Party Memos

<a id="opIdcreatePartyMemos"></a>

> Code samples

`POST /party/{partyIdentifier}/memos`

> Body parameter

```json
[
  {
    "memoTypeCode": "01",
    "severity": "S1",
    "score": 11,
    "validFrom": "03-11-1901",
    "validTo": "02-11-1915"
  }
]
```

<h3 id="create-party-memos-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyMemosRequestCmd](#schemapartymemosrequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "memoTypeCode": "01",
      "severity": "S1",
      "score": 11,
      "validFrom": "03-11-1901",
      "validTo": "02-11-1915",
      "partyMemoId": 0,
      "memoType": "string"
    }
  ]
}
```

<h3 id="create-party-memos-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Document Memos Success|[PartyMemosResponseCmd](#schemapartymemosresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Memos Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party Memos

<a id="opIdfetchMemo"></a>

> Code samples

`GET /party/{partyIdentifier}/memos/{partyMemosId}`

<h3 id="fetch-party-memos-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|integer(int64)|true|Party Identifier|
|partyMemosId|path|integer(int64)|true|Party Memos Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "memoTypeCode": "01",
    "severity": "S1",
    "score": 11,
    "validFrom": "03-11-1901",
    "validTo": "02-11-1915",
    "partyMemoId": 0,
    "memoType": "string"
  }
}
```

<h3 id="fetch-party-memos-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyMemoCmd](#schemapartymemocmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Memos

<a id="opIdupdatePartyMemos"></a>

> Code samples

`PUT /party/{partyIdentifier}/memos/{partyMemosId}/`

> Body parameter

```json
{
  "memoTypeCode": "01",
  "severity": "S1",
  "score": 11,
  "validFrom": "03-11-1901",
  "validTo": "02-11-1915",
  "partyMemoId": 1
}
```

<h3 id="update-party-memos-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyMemosId|path|integer(int64)|true|Party Memos Id|
|body|body|[PartyMemoCmd](#schemapartymemocmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-memos-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Memos Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Memos Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-fatca">Fatca</h1>

## Create Party Fatca

<a id="opIdcreatePartyFatca"></a>

> Code samples

`POST /party/{partyIdentifier}/fatca`

> Body parameter

```json
[
  {
    "placeOfIncorporation": "Da Nang",
    "countryOfIncorporation": "Vietnam",
    "countryOfResidence": "Vietnam",
    "incorporationNumber": "01",
    "boardRelNumber": "BR01",
    "reportBlNumber": "R01",
    "originalReportBlNumber": "OR1",
    "fatcaTaxId": "01",
    "documentReferenceId": "11"
  }
]
```

<h3 id="create-party-fatca-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyFatcaRequestCmd](#schemapartyfatcarequestcmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "placeOfIncorporation": "Da Nang",
      "countryOfIncorporation": "Vietnam",
      "countryOfResidence": "Vietnam",
      "incorporationNumber": "01",
      "boardRelNumber": "BR01",
      "reportBlNumber": "R01",
      "originalReportBlNumber": "OR1",
      "fatcaTaxId": "01",
      "documentReferenceId": "11",
      "partyFatcaDetailsId": 0
    }
  ]
}
```

<h3 id="create-party-fatca-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Fatca Create Success|[PartyFatcaResponseCmd](#schemapartyfatcaresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Fatca Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch All Party FatcaDetails

<a id="opIdfetchAllFatca"></a>

> Code samples

`GET /party/{partyIdentifier}/fatca/`

<h3 id="fetch-all-party-fatcadetails-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "placeOfIncorporation": "Da Nang",
      "countryOfIncorporation": "Vietnam",
      "countryOfResidence": "Vietnam",
      "incorporationNumber": "01",
      "boardRelNumber": "BR01",
      "reportBlNumber": "R01",
      "originalReportBlNumber": "OR1",
      "fatcaTaxId": "01",
      "documentReferenceId": "11",
      "partyFatcaDetailsId": 0
    }
  ]
}
```

<h3 id="fetch-all-party-fatcadetails-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyFatcaResponseCmd](#schemapartyfatcaresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party FatcaDetails

<a id="opIdfetchFatca"></a>

> Code samples

`GET /party/{partyIdentifier}/fatca/{partyFatcaDetailsId}`

<h3 id="fetch-party-fatcadetails-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyFatcaDetailsId|path|integer(int64)|true|Party Fatca Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "placeOfIncorporation": "Da Nang",
    "countryOfIncorporation": "Vietnam",
    "countryOfResidence": "Vietnam",
    "incorporationNumber": "01",
    "boardRelNumber": "BR01",
    "reportBlNumber": "R01",
    "originalReportBlNumber": "OR1",
    "fatcaTaxId": "01",
    "documentReferenceId": "11",
    "partyFatcaDetailsId": 0
  }
}
```

<h3 id="fetch-party-fatcadetails-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[PartyFatcaDetailsCmd](#schemapartyfatcadetailscmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Fatca Details

<a id="opIdupdatePartyFatca"></a>

> Code samples

`PUT /party/{partyIdentifier}/fatca/{partyFatcaDetailsId}/`

> Body parameter

```json
{
  "placeOfIncorporation": "Da Nang",
  "countryOfIncorporation": "Vietnam",
  "countryOfResidence": "Vietnam",
  "incorporationNumber": "01",
  "boardRelNumber": "BR01",
  "reportBlNumber": "R01",
  "originalReportBlNumber": "OR1",
  "fatcaTaxId": "01",
  "documentReferenceId": "11",
  "partyFatcaDetailsId": 1
}
```

<h3 id="update-party-fatca-details-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyFatcaDetailsId|path|integer(int64)|true|Party Fatca Id|
|body|body|[PartyFatcaDetailsCmd](#schemapartyfatcadetailscmd)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="update-party-fatca-details-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Party Document Update Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Document Update Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-guardians">Guardian</h1>

## Create Party Guardian

<a id="opIdcreatePartyGuardian"></a>

> Code samples

`POST /party/{partyIdentifier}/guardian`

> Body parameter

```json
[
  {
    "guardianFirstName": "Anh",
    "guardianMiddleName": "Van",
    "guardianLastName": "Duc",
    "guardianRelation": "Father",
    "guardianAddressLine1": "Da Nang",
    "guardianAddressLine2": "House 14",
    "guardianAddressLine3": "District 17",
    "guardianWardCode": "01",
    "guardianDistrictCode": "01",
    "guardianCityCode": "01"
  }
]
```

<h3 id="create-party-guardian-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|body|body|[PartyGuardianRequestCmd](#schemapartyguardiancmd)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "guardianFirstName": "Anh",
      "guardianMiddleName": "Van",
      "guardianLastName": "Duc",
      "guardianRelation": "Father",
      "guardianAddressLine1": "Da Nang",
      "guardianAddressLine2": "House 14",
      "guardianAddressLine3": "District 17",
      "guardianWardCode": "01",
      "guardianDistrictCode": "01",
      "guardianCityCode": "01",
      "partyGuardianId": 0,
      "guardianWard": "string",
      "guardianDistrict": "string",
      "guardianCity": "string"
    }
  ]
}
```

<h3 id="create-party-guardian-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Party Guardian Create Success|[PartyGuardianResponseCmd](#schemapartyguardianresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Party Guardian Create Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

## Delete Party Guardian

<a id="opIddeletePartyGuardian"></a>

> Code samples

`DELETE /party/{partyIdentifier}/guardian/{partyGuardianId}`

<h3 id="delete-party-guardian-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|partyIdentifier|path|string|true|Party Identifier|
|partyGuardianId|path|integer(int64)|true|Party GuardianId|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestID": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="delete-party-guardian-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[SuccessResponseCmd](#schemasuccessresponsecmd)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Failed|[FailureResponseCmd](#schemafailureresponsecmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[Error](#schemaerror)|

<aside class="success">
This operation does not require authentication
</aside>

# Schemas
<h2 id="schemaconfig">Config</h2>
<!-- backwards compatibility -->
<a id="schemaconfig"></a>

```json
{

"type": "UNIVERSALSEARCHFIELDS",

"configData": [

{

	"key": "EMAIL",

	"values": [

		"party.primaryEmail"

		]

	},

	{

	"key": "TEXT",

	"values": [

		"party.firstName",

		"party.lastName",

		"party.lastName",

		"party.fullName"

		]

	},

	{

	"key": "NUMBER",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "DATE",

	"values": [

		"party.dateOfBirth"

		]

	},

	{

	"key": "SPECIALCHAR",

	"values": [

		"party.dateOfBirth"

		]

	}

]

}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|config|[Config](#schemaconfig)|true|none|none|

<h2 id="schemaconfigresponse">ConfigResponse</h2>
<!-- backwards compatibility -->
<a id="schemaconfigresponse"></a>

```json
{

"meta": {

	"requestID": "b8b25dfa-0e8a-4823-b6f5-7cdbc1962615"

},

"status": {

	"code": "CONFIG_UPDATED",

	"message": "Config record persist success"

},

"data": {

	"type": "UNIVERSALSEARCHFIELDS",

	"configData": [

	{

		"key": "EMAIL",

		"values": [

		"party.primaryEmail"

		]

	},

	{

		"key": "TEXT",

		"values": [

			"party.firstName",

			"party.lastName",

			"party.lastName",

			"party.fullName"

		]

	},

	{

		"key": "NUMBER",

		"values": [

			"party.dateOfBirth"

		]

	},

	{

		"key": "DATE",

		"values": [

			"party.dateOfBirth"

		]

	},

	{

		"key": "SPECIALCHAR",

		"values": [

			"party.dateOfBirth"

		]

	  }

	 ]

	}

}
```
### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|config|[Config](#schemaconfig)|true|none|none|

<h2 id="tocS_PartyRequestCmd">PartyRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyrequestcmd"></a>
<a id="schema_PartyRequestCmd"></a>
<a id="tocSpartyrequestcmd"></a>
<a id="tocspartyrequestcmd"></a>

```json
{
  "party": {
    "partyIdentifier": "oGgCLRuEbu6kGvbB8MIuU0RShSVBHAglYoMB",
    "type": "CUSTOMER",
    "salutationCode": "01",
    "fullName": "Joi Bristo Regino",
    "firstName": "Joi",
    "middleName": "Bristo",
    "lastName": "Regino",
    "mothersMaidenName": "Mia",
    "nickName": "Jo",
    "gender": "MALE",
    "dateOfBirth": "30-11-1997",
    "placeOfBirth": "Da Nang",
    "primaryMobileNumber": "9088563701",
    "primaryEmail": "joibristo434@gmail.com",
    "maritalStatus": "SINGLE",
    "status": "ACTIVE",
    "nationalIdTypeCode": "01",
    "nationalId": "80033848",
    "referralCode": "RC01",
    "promoCode": "T01",
    "relationTypeCode": "01",
    "segmentCode": "01",
    "countryOfBirthCode": "01",
    "nationalityCode": "01",
    "educationTypeCode": "01",
    "isStaff": true,
    "staffCode": "S22",
    "groupCode": "G21",
    "portfolioCode": "PP12",
    "countryOfResidenceCode": "01",
    "residencyTypeCode": "01",
    "jobPositionTypeCode": "02",
    "amlRisk": "MEDIUM",
    "amlRiskEvalDate": "20-11-2003",
    "amlCheckStatus": false,
    "sourceSystem": "string",
    "createdBy": "SYSTEM",
    "createdByChannel": "Web"
  },
  "occupationDetail": {
    "professionCode": "01",
    "professionTypeCode": "01",
    "industryTypeCode": "11",
    "companyTypeCode": "CG09",
    "annualIncome": 91019,
    "annualTurnover": 877887,
    "taxId": "TX01",
    "dateOfInCorporation": "05-10-1980"
  },
  "address": [
    {
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01"
    }
  ],
  "contactDetails": [
    {
      "contactTypeCode": "01",
      "contactValue": "9918831234",
      "isdCode": "84",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "verified",
      "lastVerifiedDate": "30-08-1982",
      "isDnd": true
    }
  ],
  "assets": [
    {
      "assetTypeCode": "01",
      "assetName": "house",
      "potentialValue": 99999999,
      "isMortgaged": true
    }
  ],
  "memos": [
    {
      "memoTypeCode": "01",
      "severity": "S1",
      "score": 11,
      "validFrom": "03-11-1901",
      "validTo": "02-11-1915"
    }
  ],
  "risks": [
    {
      "riskTypeCode": "01",
      "riskScore": 11,
      "evaluationDate": "02-04-2090",
      "validUntil": "20-03-2098"
    }
  ],
  "fatcaDetails": [
    {
      "placeOfIncorporation": "Da Nang",
      "countryOfIncorporation": "Vietnam",
      "countryOfResidence": "Vietnam",
      "incorporationNumber": "01",
      "boardRelNumber": "BR01",
      "reportBlNumber": "R01",
      "originalReportBlNumber": "OR1",
      "fatcaTaxId": "01",
      "documentReferenceId": "11"
    }
  ],
  "guardians": [
    {
      "guardianFirstName": "Anh",
      "guardianMiddleName": "Van",
      "guardianLastName": "Duc",
      "guardianRelation": "Father",
      "guardianAddressLine1": "Da Nang",
      "guardianAddressLine2": "House 14",
      "guardianAddressLine3": "District 17",
      "guardianWardCode": "01",
      "guardianDistrictCode": "01",
      "guardianCityCode": "01"
    }
  ],
  "relations": [
    {
      "secondaryPartyId": "02",
      "partyRelationTypeCode": "01",
      "invRelation": "Y"
    }
  ],
  "documents": [
    {
      "documentTypeCode": "01",
      "documentNumber": "11",
      "documentNumberMasked": "01",
      "documentNumberToken": "02",
      "issuingDate": "14-05-1972",
      "expiryDate": "24-05-1979",
      "issuingPlace": "Dong Nai",
      "issuingCountryCode": "01",
      "issuingCountry": "vietnam",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "01",
      "verificationStatus": "verified",
      "additionalData": "Y"
    }
  ]
}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|party|[PartyCmd](#schemapartycmd)|true|none|none|
|occupationDetail|[OccupationDetailCmd](#schemaoccupationdetailcmd)|false|none|none|
|address|[Address](#schemaaddress)|false|none|none|
|contactDetails|[ContactDetails](#schemacontactdetails)|false|none|none|
|assets|[Assets](#schemaassets)|false|none|none|
|memos|[Memos](#schemamemos)|false|none|none|
|risks|[Risks](#schemarisks)|false|none|none|
|fatcaDetails|[FatcaDetails](#schemafatcadetails)|false|none|none|
|guardians|[Guardians](#schemaguardians)|false|none|none|
|relations|[Relations](#schemarelations)|false|none|none|
|xrefs|[Xrefs](#schemaxrefs)|false|none|none|
|accountMappings|[AccountMappings](#schemaaccountmappings)|false|none|none|
|documents|[Documents](#schemadocuments)|false|none|none|

<h2 id="tocS_PartyResponseCmd">PartyResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyresponsecmd"></a>
<a id="schema_PartyResponseCmd"></a>
<a id="tocSpartyresponsecmd"></a>
<a id="tocspartyresponsecmd"></a>

```json
{
  "party": {
    "partyId": 0,
    "partyType": "string",
    "salutationCode": "string",
    "salutation": "string",
    "fullName": "string",
    "firstName": "string",
    "middleName": "string",
    "lastName": "string",
    "mothersMaidenName": "string",
    "nickName": "string",
    "gender": "string",
    "dateOfBirth": "string",
    "placeOfBirth": "string",
    "primaryMobileNumber": "stringstri",
    "primaryEmail": "user@example.com",
    "maritalStatus": "string",
    "partyStatus": "string",
    "nationalIdTypeCode": "string",
    "nationalIdType": "string",
    "nationalId": "string",
    "referralCode": "string",
    "promoCode": "string",
    "relationTypeCode": "string",
    "relationType": "string",
    "partySegmentCode": "string",
    "partySegment": "string",
    "countryOfBirthCode": "string",
    "countryOfBirth": "string",
    "nationalityCode": "string",
    "nationality": "string",
    "educationTypeCode": "string",
    "educationType": "string",
    "isStaff": true,
    "staffCode": "string",
    "groupCode": "string",
    "portfolioCode": "string",
    "countryOfResidenceCode": 0,
    "countryResidence": "string",
    "residencyTypeCode": 0,
    "residencyType": "string",
    "jobPosition": "string",
    "lastRiskEvalDate": "string",
    "createdByChannel": "string",
    "updatedByChannel": "string"
  },
  "occupationDetail": {
    "professionCode": "string",
    "professionTypeCode": "string",
    "industryTypeCode": "string",
    "companyTypeCode": "string",
    "annualIncome": 0,
    "annualTurnover": 0,
    "taxId": "string",
    "dateOfInCorporation": "string"
  },
  "partyFlag": {
    "amlCheckStatus": true,
    "isDeceased": true,
    "isSolvency": true,
    "isNpa": true,
    "isWillFullDefaulter": true,
    "willFullDefaulterDate": "string",
    "isLoanOverDue": true,
    "isSuitFiled": true,
    "isPoliticallyExposed": true,
    "isFatcaApplicable": true,
    "isEmailStatementReg": true,
    "isUnderWatchList": true
  },
  "address": [
    {
      "addressTypeCode": "string",
      "isDefault": true,
      "addressLine1": "string",
      "addressLine2": "string",
      "addressLine3": "string",
      "wardCode": "string",
      "districtCode": "string",
      "cityCode": "string",
      "cityZipCode": 0,
      "provinceCode": "string",
      "countryCode": "string",
      "documentId": "string",
      "partyAddressId": 0
    }
  ],
  "contactDetails": [
    {
      "contactTypeCode": "string",
      "contactType": "string",
      "contactValue": "string",
      "isdCode": "string",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "string",
      "lastVerifiedDate": "string",
      "isDnd": true,
      "partyContactDetailsId": 0
    }
  ],
  "assets": [
    {
      "assetTypeCode": "string",
      "assetType": "string",
      "assetName": "string",
      "potentialValue": 0,
      "isMortgaged": true,
      "partyAssetId": 0
    }
  ],
  "memos": [
    {
      "memoTypeCode": "string",
      "memoType": "string",
      "severity": "string",
      "score": 0,
      "validFrom": "string",
      "validTo": "string",
      "partyMemoId": 0
    }
  ],
  "risks": [
    {
      "riskTypeCode": "string",
      "riskType": "string",
      "riskScore": 0,
      "evaluationDate": "string",
      "validUntil": "string",
      "partyRiskId": 0
    }
  ],
  "fatcaDetails": [
    {
      "placeOfIncorporation": "string",
      "countryOfIncorporation": "string",
      "countryOfResidence": "string",
      "incorporationNumber": "string",
      "boardRelNumber": "string",
      "reportBlNumber": "string",
      "originalReportBlNumber": "string",
      "fatcaTaxId": "string",
      "documentReferenceId": "string",
      "partyFatcaDetailsId": 0
    }
  ],
  "guardians": [
    {
      "guardianFirstName": "string",
      "guardianMiddleName": "string",
      "guardianLastName": "string",
      "guardianAddressLine1": "string",
      "guardianAddressLine2": "string",
      "guardianAddressLine3": "string",
      "guardianWardCode": "string",
      "guardianWard": "string",
      "guardianDistrictCode": "string",
      "guardianDistrict": "string",
      "guardianProvinceCode": "string",
      "guardianProvince": "string",
      "partyGuardianId": 0
    }
  ],
  "relations": [
    {
      "secondaryPartyId": "string",
      "partyRelationTypeCode": "string",
      "partyRelationType": "string",
      "invRelation": "string",
      "partyRelationId": 0
    }
  ],
  "xrefs": [
    {
      "systemCode": "string",
      "xrefId": "string",
      "partyXrefId": 0
    }
  ],
  "documents": [
    {
      "documentTypeCode": "string",
      "documentType": "string",
      "documentNumber": "string",
      "documentNumberMasked": "string",
      "documentNumberToken": "string",
      "issuingDate": "string",
      "expiryDate": "string",
      "issuingPlace": "string",
      "issuingCountryCode": "string",
      "issuingCountry": "string",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "string",
      "verificationStatus": "string",
      "additionalData": "string",
      "partyDocumentId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|party|[PartyCmd](#schemapartycmd)|false|none|none|
|occupationDetail|[OccupationDetailCmd](#schemaoccupationdetailcmd)|false|none|none|
|partyFlag|[PartyFlagCmd](#schemapartyflagcmd)|false|none|none|
|address|[Address](#schemaaddress)|false|none|none|
|contactDetails|[ContactDetails](#schemacontactdetails)|false|none|none|
|assets|[Assets](#schemaassets)|false|none|none|
|memos|[Memos](#schemamemos)|false|none|none|
|risks|[Risks](#schemarisks)|false|none|none|
|fatcaDetails|[FatcaDetails](#schemafatcadetails)|false|none|none|
|guardians|[Guardians](#schemaguardians)|false|none|none|
|relations|[Relations](#schemarelations)|false|none|none|
|xrefs|[Xrefs](#schemaxrefs)|false|none|none|
|accountMappings|[AccountMappings](#schemaaccountmappings)|false|none|none|
|documents|[Documents](#schemadocuments)|false|none|none|

<h2 id="tocS_PartyCmd">PartyCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartycmd"></a>
<a id="schema_PartyCmd"></a>
<a id="tocSpartycmd"></a>
<a id="tocspartycmd"></a>

```json
{
  "party": {
    "partyIdentifier": "oGgCLRuEbu6kGvbB8MIuU0RShSVBHAglYoMB",
    "type": "CUSTOMER",
    "salutationCode": "01",
    "fullName": "Joi Bristo Regino",
    "firstName": "Joi",
    "middleName": "Bristo",
    "lastName": "Regino",
    "mothersMaidenName": "Mia",
    "nickName": "Jo",
    "gender": "MALE",
    "dateOfBirth": "30-11-1997",
    "placeOfBirth": "Da Nang",
    "primaryMobileNumber": "9088563701",
    "primaryEmail": "joibristo434@gmail.com",
    "maritalStatus": "SINGLE",
    "status": "ACTIVE",
    "nationalIdTypeCode": "01",
    "nationalId": "80033848",
    "referralCode": "RC01",
    "promoCode": "T01",
    "relationTypeCode": "01",
    "segmentCode": "01",
    "countryOfBirthCode": "01",
    "nationalityCode": "01",
    "educationTypeCode": "01",
    "isStaff": true,
    "staffCode": "S22",
    "groupCode": "G21",
    "portfolioCode": "PP12",
    "countryOfResidenceCode": "01",
    "residencyTypeCode": "01",
    "jobPositionTypeCode": "02",
    "amlRisk": "MEDIUM",
    "amlRiskEvalDate": "20-11-2003",
    "amlCheckStatus": false,
    "sourceSystem": "string",
    "createdBy": "SYSTEM",
    "createdByChannel": "Web"
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|false|none|none|
|partyType|string|true|none|none|
|salutationCode|string|true|none|none|
|salutation|string|false|none|none|
|fullName|string|true|none|none|
|firstName|string|true|none|none|
|middleName|string|false|none|none|
|lastName|string|false|none|none|
|mothersMaidenName|string|false|none|none|
|nickName|string|false|none|none|
|gender|string|true|none|none|
|dateOfBirth|string|true|none|DOB in DD-MM-YYYY format|
|placeOfBirth|string|false|none|none|
|primaryMobileNumber|string|true|none|none|
|primaryEmail|string(email)|true|none|none|
|maritalStatus|string|true|none|none|
|partyStatus|string|true|none|none|
|nationalIdTypeCode|string|false|none|none|
|nationalIdType|string|false|none|none|
|nationalId|string|false|none|none|
|referralCode|string|false|none|none|
|promoCode|string|false|none|none|
|relationTypeCode|string|false|none|none|
|relationType|string|false|none|none|
|partySegmentCode|string|false|none|none|
|partySegment|string|false|none|none|
|countryOfBirthCode|string|false|none|none|
|countryOfBirth|string|false|none|none|
|nationalityCode|string|false|none|none|
|nationality|string|false|none|none|
|educationTypeCode|string|false|none|none|
|educationType|string|false|none|none|
|isStaff|boolean|false|none|none|
|staffCode|string|false|none|none|
|groupCode|string|false|none|none|
|portfolioCode|string|false|none|none|
|countryOfResidenceCode|integer|false|none|none|
|countryResidence|string|false|none|none|
|residencyTypeCode|integer|false|none|none|
|residencyType|string|false|none|none|
|jobPosition|string|false|none|none|
|lastRiskEvalDate|string|false|none|Last riskeval date in DD-MM-YYYY format|
|createdByChannel|string|false|none|none|
|updatedByChannel|string|false|none|none|

<h2 id="tocS_OccupationDetailCmd">OccupationDetailCmd</h2>
<!-- backwards compatibility -->
<a id="schemaoccupationdetailcmd"></a>
<a id="schema_OccupationDetailCmd"></a>
<a id="tocSoccupationdetailcmd"></a>
<a id="tocsoccupationdetailcmd"></a>

```json
 "occupationDetail": {
    "professionCode": "01",
    "professionTypeCode": "01",
    "industryTypeCode": "11",
    "companyTypeCode": "CG09",
    "annualIncome": 91019,
    "annualTurnover": 877887,
    "taxId": "TX01",
    "dateOfInCorporation": "05-10-1980"
  }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|professionCode|string|false|none|none|
|professionTypeCode|string|false|none|none|
|industryTypeCode|string|false|none|none|
|companyTypeCode|string|false|none|none|
|annualIncome|integer|false|none|none|
|annualTurnover|integer|false|none|none|
|taxId|string|false|none|none|
|dateOfInCorporation|string|false|none|Date Of InCorporation in DD-MM-YYYY format|

<h2 id="tocS_Address">Address</h2>
<!-- backwards compatibility -->
<a id="schemaaddress"></a>
<a id="schema_Address"></a>
<a id="tocSaddress"></a>
<a id="tocsaddress"></a>

```json
  "address": [
    {
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01"
    }
    ]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyAddressCmd](#schemapartyaddresscmd)]|false|none|none|

<h2 id="tocS_PartyAddressCmd">PartyAddressCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyaddresscmd"></a>
<a id="schema_PartyAddressCmd"></a>
<a id="tocSpartyaddresscmd"></a>
<a id="tocspartyaddresscmd"></a>

```json
{
      "addressTypeCode": "01",
      "isDefault": true,
      "addressLine1": "14/12 Ky Dong Street",
      "addressLine2": "Ward 3, District 17",
      "addressLine3": "HCMC",
      "wardCode": "01",
      "districtCode": "D01",
      "cityCode": "11",
      "cityZipCode": 11001,
      "countryCode": "84",
      "documentId": "01"
    }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|addressTypeCode|string|true|none|none|
|isDefault|boolean|false|none|none|
|addressLine1|string|true|none|none|
|addressLine2|string|true|none|none|
|addressLine3|string|false|none|none|
|wardCode|string|false|none|none|
|districtCode|string|false|none|none|
|cityCode|string|false|none|none|
|cityZipCode|integer|false|none|none|
|provinceCode|string|false|none|none|
|countryCode|string|false|none|none|
|documentId|string|false|none|none|
|partyAddressId|integer|false|none|none|

<h2 id="tocS_PartyFlagCmd">PartyFlagCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyflagcmd"></a>
<a id="schema_PartyFlagCmd"></a>
<a id="tocSpartyflagcmd"></a>
<a id="tocspartyflagcmd"></a>

```json
"partyFlag": {
      "isDeceased": true,
      "isSolvency": true,
      "isNpa": true,
      "isWillFullDefaulter": true,
      "willFullDefaulterDate": "29-12-9540",
      "isLoanOverDue": true,
      "isSuitFiled": true,
      "isPoliticallyExposed": true,
      "isFatcaApplicable": true,
      "isEmailStatementReg": true,
      "isUnderWatchList": true
     }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|isDeceased|boolean|false|none|none|
|isSolvency|boolean|false|none|none|
|isNpa|boolean|false|none|none|
|isWillFullDefaulter|boolean|false|none|none|
|willFullDefaulterDate|string|false|none|Will Full Defaulter Date in DD-MM-YYYY format|
|isLoanOverDue|boolean|false|none|none|
|isSuitFiled|boolean|false|none|none|
|isPoliticallyExposed|boolean|false|none|none|
|isFatcaApplicable|boolean|false|none|none|
|isEmailStatementReg|boolean|false|none|none|
|isUnderWatchList|boolean|false|none|none|

<h2 id="tocS_ContactDetails">ContactDetails</h2>
<!-- backwards compatibility -->
<a id="schemacontactdetails"></a>
<a id="schema_ContactDetails"></a>
<a id="tocScontactdetails"></a>
<a id="tocscontactdetails"></a>

```json
"contactDetails": [
      {
        "contactTypeCode": "01",
        "contactValue": "9918831234",
        "isdCode": "84",
        "isPrimary": true,
        "isVerified": true,
        "verifiedMode": "verified",
        "lastVerifiedDate": "30-08-1982",
        "isDnd": true,
        "partyContactDetailsId": 0,
        "contactType": "string"
      }
    ]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyContactDetailsCmd](#schemapartycontactdetailscmd)]|false|none|none|

<h2 id="tocS_PartyContactDetailsCmd">PartyContactDetailsCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartycontactdetailscmd"></a>
<a id="schema_PartyContactDetailsCmd"></a>
<a id="tocSpartycontactdetailscmd"></a>
<a id="tocspartycontactdetailscmd"></a>

```json
{
    "contactTypeCode": "01",
    "contactValue": "9918831234",
    "isdCode": "84",
    "isPrimary": true,
    "isVerified": true,
    "verifiedMode": "verified",
    "lastVerifiedDate": "30-08-1982",
    "isDnd": true,
    "partyContactDetailsId": 0,
    "contactType": "string"
    }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|contactTypeCode|string|false|none|none|
|contactType|string|false|none|none|
|contactValue|string|false|none|none|
|isdCode|string|false|none|none|
|isPrimary|boolean|false|none|none|
|isVerified|boolean|false|none|none|
|verifiedMode|string|false|none|none|
|lastVerifiedDate|string|false|none|Last Verified Date in DD-MM-YYYY format|
|isDnd|boolean|false|none|none|
|partyContactDetailsId|integer|false|none|none|


<h2 id="tocS_Assets">Assets</h2>
<!-- backwards compatibility -->
<a id="schemaassets"></a>
<a id="schema_Assets"></a>
<a id="tocSassets"></a>
<a id="tocsassets"></a>

```json
"assets": [
  {
    "assetTypeCode": "01",
    "assetName": "house",
    "potentialValue": 99999999,
    "isMortgaged": true,
    "partyAssetId": 0,
    "assetType": "string"
  }
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyAssetsCmd](#schemapartyassetscmd)]|false|none|none|

<h2 id="tocS_PartyAssetsCmd">PartyAssetsCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyassetscmd"></a>
<a id="schema_PartyAssetsCmd"></a>
<a id="tocSpartyassetscmd"></a>
<a id="tocspartyassetscmd"></a>

```json
{
"assetTypeCode": "01",
"assetName": "house",
"potentialValue": 99999999,
"isMortgaged": true,
"partyAssetId": 0,
"assetType": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|assetTypeCode|string|false|none|none|
|assetType|string|false|none|none|
|assetName|string|false|none|none|
|potentialValue|number|false|none|none|
|isMortgaged|boolean|false|none|none|
|partyAssetId|integer|false|none|none|

<h2 id="tocS_Memos">Memos</h2>
<!-- backwards compatibility -->
<a id="schemamemos"></a>
<a id="schema_Memos"></a>
<a id="tocSmemos"></a>
<a id="tocsmemos"></a>

```json
"memos": [
      {
        "memoTypeCode": "01",
        "severity": "S1",
        "score": 11,
        "validFrom": "03-11-1901",
        "validTo": "02-11-1915",
        "partyMemoId": 0,
        "memoType": "string"
      }
    ]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyMemoCmd](#schemapartymemocmd)]|false|none|none|

<h2 id="tocS_PartyMemoCmd">PartyMemoCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartymemocmd"></a>
<a id="schema_PartyMemoCmd"></a>
<a id="tocSpartymemocmd"></a>
<a id="tocspartymemocmd"></a>

```json
  {
    "memoTypeCode": "01",
    "severity": "S1",
    "score": 11,
    "validFrom": "03-11-1901",
    "validTo": "02-11-1915",
    "partyMemoId": 0,
    "memoType": "string"
  }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|memoTypeCode|string|false|none|none|
|memoType|string|false|none|none|
|severity|string|false|none|none|
|score|number|false|none|none|
|validFrom|string|false|none|Valid From in DD-MM-YYYY format|
|validTo|string|false|none|Valid To in DD-MM-YYYY format|
|partyMemoId|integer|false|none|none|

<h2 id="tocS_Risks">Risks</h2>
<!-- backwards compatibility -->
<a id="schemarisks"></a>
<a id="schema_Risks"></a>
<a id="tocSrisks"></a>
<a id="tocsrisks"></a>

```json
"risks": [
  {
    "riskTypeCode": "01",
    "riskScore": 11,
    "evaluationDate": "02-04-2090",
    "validUntil": "20-03-2098",
    "partyRiskId": 0,
    "riskType": "string"
  }
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyRiskCmd](#schemapartyriskcmd)]|false|none|none|

<h2 id="tocS_PartyRiskCmd">PartyRiskCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyriskcmd"></a>
<a id="schema_PartyRiskCmd"></a>
<a id="tocSpartyriskcmd"></a>
<a id="tocspartyriskcmd"></a>

```json
  {
    "riskTypeCode": "01",
    "riskScore": 11,
    "evaluationDate": "02-04-2090",
    "validUntil": "20-03-2098",
    "partyRiskId": 0,
    "riskType": "string"
  }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|riskTypeCode|string|false|none|none|
|riskType|string|false|none|none|
|riskScore|number|false|none|none|
|evaluationDate|string|false|none|Evaluation Date in DD-MM-YYYY format|
|validUntil|string|false|none|Valid Until in DD-MM-YYYY format|
|partyRiskId|integer|false|none|none|

<h2 id="tocS_FatcaDetails">FatcaDetails</h2>
<!-- backwards compatibility -->
<a id="schemafatcadetails"></a>
<a id="schema_FatcaDetails"></a>
<a id="tocSfatcadetails"></a>
<a id="tocsfatcadetails"></a>

```json
"fatcaDetails": [
  {
    "placeOfIncorporation": "Da Nang",
    "countryOfIncorporation": "Vietnam",
    "countryOfResidence": "Vietnam",
    "incorporationNumber": "01",
    "boardRelNumber": "BR01",
    "reportBlNumber": "R01",
    "originalReportBlNumber": "OR1",
    "fatcaTaxId": "01",
    "documentReferenceId": "11",
    "partyFatcaDetailsId": 0
  }
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyFatcaDetailsCmd](#schemapartyfatcadetailscmd)]|false|none|none|

<h2 id="tocS_PartyFatcaDetailsCmd">PartyFatcaDetailsCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyfatcadetailscmd"></a>
<a id="schema_PartyFatcaDetailsCmd"></a>
<a id="tocSpartyfatcadetailscmd"></a>
<a id="tocspartyfatcadetailscmd"></a>

```json
{
    "placeOfIncorporation": "Da Nang",
    "countryOfIncorporation": "Vietnam",
    "countryOfResidence": "Vietnam",
    "incorporationNumber": "01",
    "boardRelNumber": "BR01",
    "reportBlNumber": "R01",
    "originalReportBlNumber": "OR1",
    "fatcaTaxId": "01",
    "documentReferenceId": "11",
    "partyFatcaDetailsId": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|placeOfIncorporation|string|false|none|none|
|countryOfIncorporation|string|false|none|none|
|countryOfResidence|string|false|none|none|
|incorporationNumber|string|false|none|none|
|boardRelNumber|string|false|none|none|
|reportBlNumber|string|false|none|none|
|originalReportBlNumber|string|false|none|none|
|fatcaTaxId|string|false|none|none|
|documentReferenceId|string|false|none|none|
|partyFatcaDetailsId|integer|false|none|none|

<h2 id="tocS_Guardians">Guardians</h2>
<!-- backwards compatibility -->
<a id="schemaguardians"></a>
<a id="schema_Guardians"></a>
<a id="tocSguardians"></a>
<a id="tocsguardians"></a>

```json
"guardians": [
      {
        "guardianFirstName": "Anh",
        "guardianMiddleName": "Van",
        "guardianLastName": "Duc",
        "guardianRelation": "Father",
        "guardianAddressLine1": "Da Nang",
        "guardianAddressLine2": "House 14",
        "guardianAddressLine3": "District 17",
        "guardianWardCode": "01",
        "guardianDistrictCode": "01",
        "guardianCityCode": "01",
        "partyGuardianId": 0,
        "guardianWard": "string",
        "guardianDistrict": "string"
      }
    ]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyGuardianCmd](#schemapartyguardiancmd)]|false|none|none|

<h2 id="tocS_PartyGuardianCmd">PartyGuardianCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyguardiancmd"></a>
<a id="schema_PartyGuardianCmd"></a>
<a id="tocSpartyguardiancmd"></a>
<a id="tocspartyguardiancmd"></a>

```json
{
    "guardianFirstName": "Anh",
    "guardianMiddleName": "Van",
    "guardianLastName": "Duc",
    "guardianRelation": "Father",
    "guardianAddressLine1": "Da Nang",
    "guardianAddressLine2": "House 14",
    "guardianAddressLine3": "District 17",
    "guardianWardCode": "01",
    "guardianDistrictCode": "01",
    "guardianCityCode": "01",
    "partyGuardianId": 0,
    "guardianWard": "string",
    "guardianDistrict": "string"
  }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|guardianFirstName|string|false|none|none|
|guardianMiddleName|string|false|none|none|
|guardianLastName|string|false|none|none|
|guardianAddressLine1|string|false|none|none|
|guardianAddressLine2|string|false|none|none|
|guardianAddressLine3|string|false|none|none|
|guardianWardCode|string|false|none|none|
|guardianWard|string|false|none|none|
|guardianDistrictCode|string|false|none|none|
|guardianDistrict|string|false|none|none|
|guardianProvinceCode|string|false|none|none|
|guardianProvince|string|false|none|none|
|partyGuardianId|integer|false|none|none|

<h2 id="tocS_Relations">Relations</h2>
<!-- backwards compatibility -->
<a id="schemarelations"></a>
<a id="schema_Relations"></a>
<a id="tocSrelations"></a>
<a id="tocsrelations"></a>

```json
"relations": [
      {
        "secondaryPartyId": "02",
        "partyRelationTypeCode": "01",
        "invRelation": "Y",
        "partyRelationId": 0,
        "partyRelationType": "string"
      }
    ]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyRelationCmd](#schemapartyrelationcmd)]|false|none|none|

<h2 id="tocS_PartyRelationCmd">PartyRelationCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyrelationcmd"></a>
<a id="schema_PartyRelationCmd"></a>
<a id="tocSpartyrelationcmd"></a>
<a id="tocspartyrelationcmd"></a>

```json
{
    "secondaryPartyId": "02",
    "partyRelationTypeCode": "01",
    "invRelation": "Y",
    "partyRelationId": 0,
    "partyRelationType": "string"
  }

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|secondaryPartyId|string|false|none|none|
|partyRelationTypeCode|string|false|none|none|
|partyRelationType|string|false|none|none|
|invRelation|string|false|none|none|
|partyRelationId|integer|false|none|none|

<h2 id="tocS_Xrefs">Xrefs</h2>
<!-- backwards compatibility -->
<a id="schemaxrefs"></a>
<a id="schema_Xrefs"></a>
<a id="tocSxrefs"></a>
<a id="tocsxrefs"></a>

```json
[
  {
    "systemCode": "string",
    "xrefId": "string",
    "partyXrefId": 0
  }
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyXrefCmd](#schemapartyxrefcmd)]|false|none|none|

<h2 id="tocS_PartyXrefCmd">PartyXrefCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyxrefcmd"></a>
<a id="schema_PartyXrefCmd"></a>
<a id="tocSpartyxrefcmd"></a>
<a id="tocspartyxrefcmd"></a>

```json
{
  "systemCode": "string",
  "xrefId": "string",
  "partyXrefId": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|systemCode|string|false|none|none|
|xrefId|string|false|none|none|
|partyXrefId|integer|false|none|none|

<h2 id="tocS_AccountMappings">AccountMappings</h2>
<!-- backwards compatibility -->
<a id="schemaaccountmappings"></a>
<a id="schema_AccountMappings"></a>
<a id="tocSaccountmappings"></a>
<a id="tocsaccountmappings"></a>

```json
[
  {
    "accountId": "string",
    "relation": "string",
    "partyAccountMappingId": 0
  }
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyAccountMappingCmd](#schemapartyaccountmappingcmd)]|false|none|none|

<h2 id="tocS_PartyAccountMappingCmd">PartyAccountMappingCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyaccountmappingcmd"></a>
<a id="schema_PartyAccountMappingCmd"></a>
<a id="tocSpartyaccountmappingcmd"></a>
<a id="tocspartyaccountmappingcmd"></a>

```json
{
  "accountId": "string",
  "relation": "string",
  "partyAccountMappingId": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|accountId|string|false|none|none|
|relation|string|false|none|none|
|partyAccountMappingId|integer|false|none|none|

<h2 id="tocS_Documents">Documents</h2>
<!-- backwards compatibility -->
<a id="schemadocuments"></a>
<a id="schema_Documents"></a>
<a id="tocSdocuments"></a>
<a id="tocsdocuments"></a>

```json
[
  {
    "documentTypeCode": "string",
    "documentType": "string",
    "documentNumber": "string",
    "documentNumberMasked": "string",
    "documentNumberToken": "string",
    "issuingDate": "string",
    "expiryDate": "string",
    "issuingPlace": "string",
    "issuingCountryCode": "string",
    "issuingCountry": "string",
    "isPoi": true,
    "isPoa": true,
    "dmsReferenceId": "string",
    "verificationStatus": "string",
    "additionalData": "string",
    "partyDocumentId": 0
  }
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PartyDocumentCmd](#schemapartydocumentcmd)]|false|none|none|

<h2 id="tocS_PartyDocumentCmd">PartyDocumentCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartydocumentcmd"></a>
<a id="schema_PartyDocumentCmd"></a>
<a id="tocSpartydocumentcmd"></a>
<a id="tocspartydocumentcmd"></a>

```json
{
  "documentTypeCode": "string",
  "documentType": "string",
  "documentNumber": "string",
  "documentNumberMasked": "string",
  "documentNumberToken": "string",
  "issuingDate": "string",
  "expiryDate": "string",
  "issuingPlace": "string",
  "issuingCountryCode": "string",
  "issuingCountry": "string",
  "isPoi": true,
  "isPoa": true,
  "dmsReferenceId": "string",
  "verificationStatus": "string",
  "additionalData": "string",
  "partyDocumentId": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|documentTypeCode|string|false|none|none|
|documentType|string|false|none|none|
|documentNumber|string|false|none|none|
|documentNumberMasked|string|false|none|none|
|documentNumberToken|string|false|none|none|
|issuingDate|string|false|none|Issuing Date in DD-MM-YYYY format|
|expiryDate|string|false|none|Expiry Date in DD-MM-YYYY format|
|issuingPlace|string|false|none|none|
|issuingCountryCode|string|false|none|none|
|issuingCountry|string|false|none|none|
|isPoi|boolean|false|none|none|
|isPoa|boolean|false|none|none|
|dmsReferenceId|string|false|none|none|
|verificationStatus|string|false|none|none|
|additionalData|string|false|none|none|
|partyDocumentId|integer|false|none|none|

<h2 id="tocS_PartyDistinctiveSearchCmd">PartyDistinctiveSearchCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartydistinctivesearchcmd"></a>
<a id="schema_PartyDistinctiveSearchCmd"></a>
<a id="tocSpartydistinctivesearchcmd"></a>
<a id="tocspartydistinctivesearchcmd"></a>

```json
{
  "partyId": 0,
  "firstName": "string",
  "lastName": "string",
  "mobileNumber": "stringstri",
  "emailId": "string",
  "dateOfBirth": "string",
  "limit": 0,
  "offset": 0,
  "sortingOrder": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|false|none|none|
|firstName|string|false|none|none|
|lastName|string|false|none|none|
|mobileNumber|string|false|none|none|
|emailId|string|false|none|none|
|dateOfBirth|string|false|none|Date of birth in DD-MM-YYYY format|
|limit|integer|false|none|none|
|offset|integer|false|none|none|
|sortingOrder|string|false|none|none|

<h2 id="tocS_PartySearchResponseCmd">PartySearchResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartysearchresponsecmd"></a>
<a id="schema_PartySearchResponseCmd"></a>
<a id="tocSpartysearchresponsecmd"></a>
<a id="tocspartysearchresponsecmd"></a>

```json
{
  "limit": 0,
  "offset": 0,
  "total": 0,
  "data": [
    {
      "partyId": 0,
      "firstName": "string",
      "middleName": "string",
      "lastName": "string",
      "dateOfBirth": "string",
      "mobileNumber": "string",
      "emailId": "string",
      "nationality": "string"
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|limit|integer|false|none|none|
|offset|integer|false|none|none|
|total|integer(int64)|false|none|none|
|data|[[PartyRecordCmd](#schemapartyrecordcmd)]|false|none|none|

<h2 id="tocS_PartyRecordCmd">PartyRecordCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyrecordcmd"></a>
<a id="schema_PartyRecordCmd"></a>
<a id="tocSpartyrecordcmd"></a>
<a id="tocspartyrecordcmd"></a>

```json
{
  "partyId": 0,
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "dateOfBirth": "string",
  "mobileNumber": "string",
  "emailId": "string",
  "nationality": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|false|none|none|
|firstName|string|false|none|none|
|middleName|string|false|none|none|
|lastName|string|false|none|none|
|dateOfBirth|string|false|none|none|
|mobileNumber|string|false|none|none|
|emailId|string|false|none|none|
|nationality|string|false|none|none|

<h2 id="tocS_DedupeRequestCmd">DedupeRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemadeduperequestcmd"></a>
<a id="schema_DedupeRequestCmd"></a>
<a id="tocSdeduperequestcmd"></a>
<a id="tocsdeduperequestcmd"></a>

```json
{
  "mobileNumber": "stringstri"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|mobileNumber|string|true|none|none|

<h2 id="tocS_DedupeResponseCmd">DedupeResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemadeduperesponsecmd"></a>
<a id="schema_DedupeResponseCmd"></a>
<a id="tocSdeduperesponsecmd"></a>
<a id="tocsdeduperesponsecmd"></a>

```json
{
  "data": [
    {
      "partyId": 0,
      "firstName": "string",
      "middleName": "string",
      "lastName": "string",
      "dateOfBirth": "string",
      "primaryMobileNumber": "string",
      "primaryEmail": "string"
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|data|[[PartyDetailCmd](#schemapartydetailcmd)]|false|none|none|

<h2 id="tocS_PartyDetailCmd">PartyDetailCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartydetailcmd"></a>
<a id="schema_PartyDetailCmd"></a>
<a id="tocSpartydetailcmd"></a>
<a id="tocspartydetailcmd"></a>

```json
{
  "partyId": 0,
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "dateOfBirth": "string",
  "primaryMobileNumber": "string",
  "primaryEmail": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|false|none|none|
|firstName|string|false|none|none|
|middleName|string|false|none|none|
|lastName|string|false|none|none|
|dateOfBirth|string|false|none|none|
|primaryMobileNumber|string|false|none|none|
|primaryEmail|string|false|none|none|

<h2 id="tocS_BadRequestError">BadRequestError</h2>
<!-- backwards compatibility -->
<a id="schemabadrequesterror"></a>
<a id="schema_BadRequestError"></a>
<a id="tocSbadrequesterror"></a>
<a id="tocsbadrequesterror"></a>

```json
[
  {}
]

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[FieldError](#schemafielderror)]|false|none|none|

<h2 id="tocS_FieldError">FieldError</h2>
<!-- backwards compatibility -->
<a id="schemafielderror"></a>
<a id="schema_FieldError"></a>
<a id="tocSfielderror"></a>
<a id="tocsfielderror"></a>

```json
{}

```

### Properties

*None*

<h2 id="tocS_Error">Error</h2>
<!-- backwards compatibility -->
<a id="schemaerror"></a>
<a id="schema_Error"></a>
<a id="tocSerror"></a>
<a id="tocserror"></a>

```json
{}

```

### Properties

*None*

<h2 id="tocS_SuccessResponseCmd">SuccessResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemasuccessresponsecmd"></a>
<a id="schema_SuccessResponseCmd"></a>
<a id="tocSsuccessresponsecmd"></a>
<a id="tocssuccessresponsecmd"></a>

```json
{
  "code": 0,
  "partyId": 0,
  "message": "string",
  "details": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|code|integer|false|none|none|
|partyId|integer|false|none|none|
|message|string|false|none|none|
|details|string|false|none|none|

<h2 id="tocS_FailureResponseCmd">FailureResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemafailureresponsecmd"></a>
<a id="schema_FailureResponseCmd"></a>
<a id="tocSfailureresponsecmd"></a>
<a id="tocsfailureresponsecmd"></a>

```json
{
  "code": 0,
  "message": "string",
  "details": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|code|integer|false|none|none|
|message|string|false|none|none|
|details|string|false|none|none|

<h2 id="tocS_PartyContactUpdateCmd">PartyContactUpdateCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartycontactupdatecmd"></a>
<a id="schema_PartyContactUpdateCmd"></a>
<a id="tocSpartycontactupdatecmd"></a>
<a id="tocspartycontactupdatecmd"></a>

```json
{
  "partyId": 0,
  "partyType": "string",
  "partyStatus": "string",
  "partyAddress": {
    "addressTypeCode": "string",
    "isDefault": true,
    "addressLine1": "string",
    "addressLine2": "string",
    "addressLine3": "string",
    "wardCode": "string",
    "districtCode": "string",
    "cityCode": "string",
    "cityZipCode": 0,
    "provinceCode": "string",
    "countryCode": "string",
    "documentId": "string",
    "partyAddressId": 0
  },
  "partyMobile": {
    "contactTypeCode": "string",
    "mobileNumber": "string",
    "countryId": 0,
    "isPrimary": true
  },
  "partyEmail": {
    "contactTypeCode": "string",
    "emailId": "string",
    "isPrimary": true
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|true|none|none|
|partyType|string|true|none|none|
|partyStatus|string|false|none|none|
|partyAddress|[PartyAddressCmd](#schemapartyaddresscmd)|false|none|none|
|partyMobile|[PartyMobileCmd](#schemapartymobilecmd)|false|none|none|
|partyEmail|[PartyEmailCmd](#schemapartyemailcmd)|false|none|none|

<h2 id="tocS_PartyMobileCmd">PartyMobileCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartymobilecmd"></a>
<a id="schema_PartyMobileCmd"></a>
<a id="tocSpartymobilecmd"></a>
<a id="tocspartymobilecmd"></a>

```json
{
  "contactTypeCode": "string",
  "mobileNumber": "string",
  "countryId": 0,
  "isPrimary": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|contactTypeCode|string|false|none|none|
|mobileNumber|string|false|none|none|
|countryId|integer|false|none|none|
|isPrimary|boolean|false|none|none|

<h2 id="tocS_PartyEmailCmd">PartyEmailCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyemailcmd"></a>
<a id="schema_PartyEmailCmd"></a>
<a id="tocSpartyemailcmd"></a>
<a id="tocspartyemailcmd"></a>

```json
{
  "contactTypeCode": "string",
  "emailId": "string",
  "isPrimary": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|contactTypeCode|string|false|none|none|
|emailId|string|false|none|none|
|isPrimary|boolean|false|none|none|

<h2 id="tocS_PartyAddressRequestCmd">PartyAddressRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyaddressrequestcmd"></a>
<a id="schema_PartyAddressRequestCmd"></a>
<a id="tocSpartyaddressrequestcmd"></a>
<a id="tocspartyaddressrequestcmd"></a>

```json
{
  "address": [
    {
      "addressTypeCode": "string",
      "isDefault": true,
      "addressLine1": "string",
      "addressLine2": "string",
      "addressLine3": "string",
      "wardCode": "string",
      "districtCode": "string",
      "cityCode": "string",
      "cityZipCode": 0,
      "provinceCode": "string",
      "countryCode": "string",
      "documentId": "string",
      "partyAddressId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|address|[Address](#schemaaddress)|true|none|none|

<h2 id="tocS_PartyAddressResponseCmd">PartyAddressResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyaddressresponsecmd"></a>
<a id="schema_PartyAddressResponseCmd"></a>
<a id="tocSpartyaddressresponsecmd"></a>
<a id="tocspartyaddressresponsecmd"></a>

```json
{
  "address": [
    {
      "addressTypeCode": "string",
      "isDefault": true,
      "addressLine1": "string",
      "addressLine2": "string",
      "addressLine3": "string",
      "wardCode": "string",
      "districtCode": "string",
      "cityCode": "string",
      "cityZipCode": 0,
      "provinceCode": "string",
      "countryCode": "string",
      "documentId": "string",
      "partyAddressId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|address|[Address](#schemaaddress)|true|none|none|

<h2 id="tocS_PartyContactsRequestCmd">PartyContactsRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartycontactsrequestcmd"></a>
<a id="schema_PartyContactsRequestCmd"></a>
<a id="tocSpartycontactsrequestcmd"></a>
<a id="tocspartycontactsrequestcmd"></a>

```json
{
  "contacts": [
    {
      "contactTypeCode": "string",
      "contactType": "string",
      "contactValue": "string",
      "isdCode": "string",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "string",
      "lastVerifiedDate": "string",
      "isDnd": true,
      "partyContactDetailsId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|contacts|[ContactDetails](#schemacontactdetails)|true|none|none|

<h2 id="tocS_PartyContactsResponseCmd">PartyContactsResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartycontactsresponsecmd"></a>
<a id="schema_PartyContactsResponseCmd"></a>
<a id="tocSpartycontactsresponsecmd"></a>
<a id="tocspartycontactsresponsecmd"></a>

```json
{
  "contacts": [
    {
      "contactTypeCode": "string",
      "contactType": "string",
      "contactValue": "string",
      "isdCode": "string",
      "isPrimary": true,
      "isVerified": true,
      "verifiedMode": "string",
      "lastVerifiedDate": "string",
      "isDnd": true,
      "partyContactDetailsId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|contacts|[ContactDetails](#schemacontactdetails)|true|none|none|

<h2 id="tocS_PartyRisksRequestCmd">PartyRisksRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyrisksrequestcmd"></a>
<a id="schema_PartyRisksRequestCmd"></a>
<a id="tocSpartyrisksrequestcmd"></a>
<a id="tocspartyrisksrequestcmd"></a>

```json
{
  "risks": [
    {
      "riskTypeCode": "string",
      "riskType": "string",
      "riskScore": 0,
      "evaluationDate": "string",
      "validUntil": "string",
      "partyRiskId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|risks|[Risks](#schemarisks)|true|none|none|

<h2 id="tocS_PartyRisksResponseCmd">PartyRisksResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyrisksresponsecmd"></a>
<a id="schema_PartyRisksResponseCmd"></a>
<a id="tocSpartyrisksresponsecmd"></a>
<a id="tocspartyrisksresponsecmd"></a>

```json
{
  "risks": [
    {
      "riskTypeCode": "string",
      "riskType": "string",
      "riskScore": 0,
      "evaluationDate": "string",
      "validUntil": "string",
      "partyRiskId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|risks|[Risks](#schemarisks)|true|none|none|

<h2 id="tocS_PartyDocumentsRequestCmd">PartyDocumentsRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartydocumentsrequestcmd"></a>
<a id="schema_PartyDocumentsRequestCmd"></a>
<a id="tocSpartydocumentsrequestcmd"></a>
<a id="tocspartydocumentsrequestcmd"></a>

```json
{
  "documents": [
    {
      "documentTypeCode": "string",
      "documentType": "string",
      "documentNumber": "string",
      "documentNumberMasked": "string",
      "documentNumberToken": "string",
      "issuingDate": "string",
      "expiryDate": "string",
      "issuingPlace": "string",
      "issuingCountryCode": "string",
      "issuingCountry": "string",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "string",
      "verificationStatus": "string",
      "additionalData": "string",
      "partyDocumentId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|documents|[Documents](#schemadocuments)|true|none|none|

<h2 id="tocS_PartyDocumentsResponseCmd">PartyDocumentsResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartydocumentsresponsecmd"></a>
<a id="schema_PartyDocumentsResponseCmd"></a>
<a id="tocSpartydocumentsresponsecmd"></a>
<a id="tocspartydocumentsresponsecmd"></a>

```json
{
  "documents": [
    {
      "documentTypeCode": "string",
      "documentType": "string",
      "documentNumber": "string",
      "documentNumberMasked": "string",
      "documentNumberToken": "string",
      "issuingDate": "string",
      "expiryDate": "string",
      "issuingPlace": "string",
      "issuingCountryCode": "string",
      "issuingCountry": "string",
      "isPoi": true,
      "isPoa": true,
      "dmsReferenceId": "string",
      "verificationStatus": "string",
      "additionalData": "string",
      "partyDocumentId": 0
    }
  ]
}

```

<h2 id="tocS_PartyMemosRequestCmd">PartyMemosRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartymemosrequestcmd"></a>
<a id="schema_PartyMemosRequestCmd"></a>
<a id="tocSpartymemosrequestcmd"></a>
<a id="tocspartymemosrequestcmd"></a>

```json
{
  "memos": [
    {
      "memoTypeCode": "string",
      "memoType": "string",
      "severity": "string",
      "score": 0,
      "validFrom": "string",
      "validTo": "string",
      "partyMemoId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|memos|[Memos](#schemamemos)|true|none|none|

<h2 id="tocS_PartyMemosResponseCmd">PartyMemosResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartymemosresponsecmd"></a>
<a id="schema_PartyMemosResponseCmd"></a>
<a id="tocSpartymemosresponsecmd"></a>
<a id="tocspartymemosresponsecmd"></a>

```json
{
  "memos": [
    {
      "memoTypeCode": "string",
      "memoType": "string",
      "severity": "string",
      "score": 0,
      "validFrom": "string",
      "validTo": "string",
      "partyMemoId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|memos|[Memos](#schemamemos)|true|none|none|

<h2 id="tocS_PartyFatcaRequestCmd">PartyFatcaRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyfatcarequestcmd"></a>
<a id="schema_PartyFatcaRequestCmd"></a>
<a id="tocSpartyfatcarequestcmd"></a>
<a id="tocspartyfatcarequestcmd"></a>

```json
{
  "fatcaDetails": [
    {
      "placeOfIncorporation": "string",
      "countryOfIncorporation": "string",
      "countryOfResidence": "string",
      "incorporationNumber": "string",
      "boardRelNumber": "string",
      "reportBlNumber": "string",
      "originalReportBlNumber": "string",
      "fatcaTaxId": "string",
      "documentReferenceId": "string",
      "partyFatcaDetailsId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|fatcaDetails|[FatcaDetails](#schemafatcadetails)|true|none|none|

<h2 id="tocS_PartyFatcaResponseCmd">PartyFatcaResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyfatcaresponsecmd"></a>
<a id="schema_PartyFatcaResponseCmd"></a>
<a id="tocSpartyfatcaresponsecmd"></a>
<a id="tocspartyfatcaresponsecmd"></a>

```json
{
  "fatcaDetails": [
    {
      "placeOfIncorporation": "string",
      "countryOfIncorporation": "string",
      "countryOfResidence": "string",
      "incorporationNumber": "string",
      "boardRelNumber": "string",
      "reportBlNumber": "string",
      "originalReportBlNumber": "string",
      "fatcaTaxId": "string",
      "documentReferenceId": "string",
      "partyFatcaDetailsId": 0
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|fatcaDetails|[FatcaDetails](#schemafatcadetails)|true|none|none|

<h2 id="tocS_PartyGuardianRequestCmd">PartyGuardianRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyguardianrequestcmd"></a>
<a id="schema_PartyGuadianRequestCmd"></a>
<a id="tocSpartyguardianrequestcmd"></a>
<a id="tocspartyguardianrequestcmd"></a>

```json
{
  "guardians": [
   {
    "guardianFirstName": "Anh",
    "guardianMiddleName": "Van",
    "guardianLastName": "Duc",
    "guardianRelation": "Father",
    "guardianAddressLine1": "Da Nang",
    "guardianAddressLine2": "House 14",
    "guardianAddressLine3": "District 17",
    "guardianWardCode": "01",
    "guardianDistrictCode": "01",
    "guardianCityCode": "01"
  }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|guardians|[Guardians](#schemaguardians)|true|none|none|

<h2 id="tocS_PartyGuardianResponseCmd">PartyGuardianResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemapartyguardianresponsecmd"></a>
<a id="schema_PartyGuardianResponseCmd"></a>
<a id="tocSpartyguardianresponsecmd"></a>
<a id="tocspartyguardianresponsecmd"></a>

```json
{
  "guardians": [
    {
      "guardianFirstName": "Anh",
      "guardianMiddleName": "Van",
      "guardianLastName": "Duc",
      "guardianRelation": "Father",
      "guardianAddressLine1": "Da Nang",
      "guardianAddressLine2": "House 14",
      "guardianAddressLine3": "District 17",
      "guardianWardCode": "01",
      "guardianDistrictCode": "01",
      "guardianCityCode": "01",
      "partyGuardianId": 0,
      "guardianWard": "string",
      "guardianDistrict": "string",
      "guardianCity": "string"
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|guardians|[Guardians](#schemaguardians)|true|none|none|

<h2 id="tocS_DedupeFieldRequestCmd">DedupeFieldRequestCmd</h2>
<!-- backwards compatibility -->
<a id="schemadedupefieldrequestcmd"></a>
<a id="schema_DedupeFieldRequestCmd"></a>
<a id="tocSdedupefieldrequestcmd"></a>
<a id="tocsdedupefieldrequestcmd"></a>

```json
{
  "countryCode": "string",
  "fullName": "string",
  "mobileNumber": "stringstri",
  "dateOfBirth": "string",
  "nationalIdTypeCode": "string",
  "nationalId": "string",
  "nationalityCode": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|countryCode|string|false|none|none|
|fullName|string|false|none|none|
|mobileNumber|string|false|none|none|
|dateOfBirth|string|false|none|Date of birth in DD-MM-YYYY format|
|nationalIdTypeCode|string|false|none|none|
|nationalId|string|false|none|none|
|nationalityCode|string|false|none|none|

<h2 id="tocS_DedupeFieldResponseCmd">DedupeFieldResponseCmd</h2>
<!-- backwards compatibility -->
<a id="schemadedupefieldresponsecmd"></a>
<a id="schema_DedupeFieldResponseCmd"></a>
<a id="tocSdedupefieldresponsecmd"></a>
<a id="tocsdedupefieldresponsecmd"></a>

```json
{
  "info": "string",
  "result": [
    {
      "party": {
        "partyId": 0,
        "partyType": "string",
        "salutationCode": "string",
        "salutation": "string",
        "fullName": "string",
        "firstName": "string",
        "middleName": "string",
        "lastName": "string",
        "mothersMaidenName": "string",
        "nickName": "string",
        "gender": "string",
        "dateOfBirth": "string",
        "placeOfBirth": "string",
        "primaryMobileNumber": "stringstri",
        "primaryEmail": "user@example.com",
        "maritalStatus": "string",
        "partyStatus": "string",
        "nationalIdTypeCode": "string",
        "nationalIdType": "string",
        "nationalId": "string",
        "referralCode": "string",
        "promoCode": "string",
        "relationTypeCode": "string",
        "relationType": "string",
        "partySegmentCode": "string",
        "partySegment": "string",
        "countryOfBirthCode": "string",
        "countryOfBirth": "string",
        "nationalityCode": "string",
        "nationality": "string",
        "educationTypeCode": "string",
        "educationType": "string",
        "isStaff": true,
        "staffCode": "string",
        "groupCode": "string",
        "portfolioCode": "string",
        "countryOfResidenceCode": 0,
        "countryResidence": "string",
        "residencyTypeCode": 0,
        "residencyType": "string",
        "jobPosition": "string",
        "lastRiskEvalDate": "string",
        "createdByChannel": "string",
        "updatedByChannel": "string"
      },
      "occupationDetail": {
        "professionCode": "string",
        "professionTypeCode": "string",
        "industryTypeCode": "string",
        "companyTypeCode": "string",
        "annualIncome": 0,
        "annualTurnover": 0,
        "taxId": "string",
        "dateOfInCorporation": "string"
      },
      "partyFlag": {
        "amlCheckStatus": true,
        "isDeceased": true,
        "isSolvency": true,
        "isNpa": true,
        "isWillFullDefaulter": true,
        "willFullDefaulterDate": "string",
        "isLoanOverDue": true,
        "isSuitFiled": true,
        "isPoliticallyExposed": true,
        "isFatcaApplicable": true,
        "isEmailStatementReg": true,
        "isUnderWatchList": true
      },
      "address": [
        {
          "addressTypeCode": "string",
          "isDefault": true,
          "addressLine1": "string",
          "addressLine2": "string",
          "addressLine3": "string",
          "wardCode": "string",
          "districtCode": "string",
          "cityCode": "string",
          "cityZipCode": 0,
          "provinceCode": "string",
          "countryCode": "string",
          "documentId": "string",
          "partyAddressId": 0
        }
      ],
      "contactDetails": [
        {
          "contactTypeCode": "string",
          "contactType": "string",
          "contactValue": "string",
          "isdCode": "string",
          "isPrimary": true,
          "isVerified": true,
          "verifiedMode": "string",
          "lastVerifiedDate": "string",
          "isDnd": true,
          "partyContactDetailsId": 0
        }
      ],
     
      "assets": [
        {
          "assetTypeCode": "string",
          "assetType": "string",
          "assetName": "string",
          "potentialValue": 0,
          "isMortgaged": true,
          "partyAssetId": 0
        }
      ],
      "memos": [
        {
          "memoTypeCode": "string",
          "memoType": "string",
          "severity": "string",
          "score": 0,
          "validFrom": "string",
          "validTo": "string",
          "partyMemoId": 0
        }
      ],
      "risks": [
        {
          "riskTypeCode": "string",
          "riskType": "string",
          "riskScore": 0,
          "evaluationDate": "string",
          "validUntil": "string",
          "partyRiskId": 0
        }
      ],
      "fatcaDetails": [
        {
          "placeOfIncorporation": "string",
          "countryOfIncorporation": "string",
          "countryOfResidence": "string",
          "incorporationNumber": "string",
          "boardRelNumber": "string",
          "reportBlNumber": "string",
          "originalReportBlNumber": "string",
          "fatcaTaxId": "string",
          "documentReferenceId": "string",
          "partyFatcaDetailsId": 0
        }
      ],
      "guardians": [
        {
          "guardianFirstName": "string",
          "guardianMiddleName": "string",
          "guardianLastName": "string",
          "guardianAddressLine1": "string",
          "guardianAddressLine2": "string",
          "guardianAddressLine3": "string",
          "guardianWardCode": "string",
          "guardianWard": "string",
          "guardianDistrictCode": "string",
          "guardianDistrict": "string",
          "guardianCityCode": "string",
          "guardianCity": "string",
          "partyGuardianId": 0
        }
      ],
      "relations": [
        {
          "secondaryPartyId": "string",
          "partyRelationTypeCode": "string",
          "partyRelationType": "string",
          "invRelation": "string",
          "partyRelationId": 0
        }
      ],
      "xrefs": [
        {
          "systemCode": "string",
          "xrefId": "string",
          "partyXrefId": 0
        }
      ],
      "documents": [
        {
          "documentTypeCode": "string",
          "documentType": "string",
          "documentNumber": "string",
          "documentNumberMasked": "string",
          "documentNumberToken": "string",
          "issuingDate": "string",
          "expiryDate": "string",
          "issuingPlace": "string",
          "issuingCountryCode": "string",
          "issuingCountry": "string",
          "isPoi": true,
          "isPoa": true,
          "dmsReferenceId": "string",
          "verificationStatus": "string",
          "additionalData": "string",
          "partyDocumentId": 0
        }
      ]
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|info|string|false|none|none|
|result|[[PartyResponseCmd](#schemapartyresponsecmd)]|false|none|none|
