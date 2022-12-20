
## Open API Specification for Party v1.0.0

Scroll down for code samples, example requests and responses.


<h1 id="open-api-specification-for-party-nominees">Nominees</h1>

## Create Party Nominee

<a id="opIdcreatePartyNominees"></a>

> Code samples

`POST /nominee`

> Body parameter

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "salutation": "Mr.",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "relation": "FATHER",
  "nationalId": "string"
}

```

<h3 id="create-party-nominees-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[NomineeRequest](#schemanomineerequest)|true|none|

> Example responses

> 201 Response

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "partyId": 0,
    "nomineeId": 0,
    "salutation": "string",
    "firstName": "string",
    "middleName": "string",
    "lastName": "string",
    "relation": "string",
    "nationalId": "string"
  }
}

```

<h3 id="create-party-nominees-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Nominee Create Success|[NomineeResponse](#schemanomineeresponse)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[ResponseApi](#schemaresponseapi)|

<aside class="success">
This operation does not require authentication
</aside>

## Update Party Nominee

<a id="opIdupdatePartyNominee"></a>

> Code samples

`PUT /nominee/`

> Body parameter

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "salutation": "Mr.",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "relation": "FATHER",
  "nationalId": "string"
}
```

<h3 id="update-party-nominee-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[nomineeRequest](#schemanomineerequest)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {
    "partyId": 0,
    "nomineeId": 0,
    "salutation": "string",
    "firstName": "string",
    "middleName": "string",
    "lastName": "string",
    "relation": "string",
    "nationalId": "string"
  }
}
```

<h3 id="update-party-nominee-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Nominee Updated Response|[NomineeResponse](#schemanomineeresponse)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[ResponseApi](#schemaresponseapi)|
<aside class="success">
This operation does not require authentication
</aside>

## Fetch Party Nominee

<a id="opIdfetchPartyNominee"></a>

> Code samples

`GET /nominee/fetch`

> Body parameter

```json
{
  "partyId": 1,
  "nomineeId": 0
}
```


<h3 id="fetch-party-nominee-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[FetchNomineeRequest](#schemafetchnomineesrequest)|true|none|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": [
    {
      "partyId": 0,
      "nomineeId": 0,
      "salutation": "string",
      "firstName": "string",
      "middleName": "string",
      "lastName": "string",
      "relation": "string",
      "nationalId": "string"
    }
  ]
}
```

<h3 id="fetch-party-nominee-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[NomineeFetchResponse](#schemanomineefetchresponse)|
|204|[NO CONTENT](https://tools.ietf.org/html/rfc7231#section-6.3.5)|Failed|[ResponseApi](#schemaresponseapi)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[ResponseApi](#schemaresponseapi)|

<aside class="success">
This operation does not require authentication
</aside>

## Delete Party Nominee

<a id="opIddeletePartyNominee"></a>

> Code samples

`DELETE /nominee/{nomineeId}`

<h3 id="delete-party-nominee-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|nomineeId|path|integer(int64)|true|Nominee Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  }
}
```

<h3 id="delete-party-nominee-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|Success|[ResponseApi](#schemapartynomineescmd)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[ResponseApi](#schemaresponseapi)|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="open-api-specification-for-party-nominees">Nominee Mapping</h1>

## Create Party Nominee Mapping

<a id="opIdcreatePartyNomineeMapping"></a>

> Code samples

`POST /nominee/mapping`

> Body parameter

```json
{
  "partyId": 0,
  "nomineeId": 0,
  "accountNumber": "string"
}

```

<h3 id="create-party-nominee-mapping-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[NomineeMappingRequest](#schemanomineemappingrequest)|true|none|


> Example responses

> 201 Response

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": { }
}

```

<h3 id="create-party-nominees-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|201|[Created](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Nominee Mapping Create Success|[SucessResponseApi](#schemasuccessresponseapi)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Nominee Mapping Create Failed|[ResponseApi](#schemaresponseapi)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[ResponseApi](#schemaresponseapi)|

<aside class="success">
This operation does not require authentication
</aside>

## Delete Party Nominee Mapping

<a id="opIddeletePartyNomineeMapping"></a>

> Code samples

`DELETE /nominee/mapping/{nomineeMappingId}`

<h3 id="delete-party-nominee-mapping-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|nomineeMappingId|path|integer(int64)|true|Nominee Mapping Id|

> Example responses

> 200 Response

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": {}
}
```

<h3 id="delete-party-nominee-mapping-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.2)|Nominee Mapping Delete Success|[SucessResponseApi](#schemasuccessresponseapi)|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Nominee Mapping Delete Failed|[ResponseApi](#schemaresponseapi)|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|Internal Server Error|[ResponseApi](#schemaresponseapi)|


<aside class="success">
This operation does not require authentication
</aside>

<h2 id="tocS_Nominees">Nominee</h2>
<!-- backwards compatibility -->
<a id="schemanominees"></a>
<a id="schema_Nominees"></a>
<a id="tocSnominees"></a>
<a id="tocsnominees"></a>

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "salutation": "Mr.",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "relation": "FATHER",
  "nationalId": "string"
}
```


### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|false|none|none|
|nomineeId|integer|false|none|none|
|salutation|string|false|none|none|
|firstName|string|false|none|none|
|middleName|string|false|none|none|
|lastName|string|false|none|none|
|relation|string|false|none|none|
|nationalId|string|false|none|none|

<h2 id="tocS_NomineeRequest">NomineeRequest</h2>
<!-- backwards compatibility -->
<a id="schemanomineerequest"></a>
<a id="schema_NomineeRequest"></a>
<a id="tocSnomineerequest"></a>
<a id="tocsnomineerequest"></a>

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "salutation": "Mr.",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "relation": "FATHER",
  "nationalId": "string"
}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[Nominee](#schemanominees)]|false|none|none|

<h2 id="tocS_FetchNomineeRequest">FetchNomineeRequest</h2>
<!-- backwards compatibility -->
<a id="schemafetchnomineesrequest"></a>
<a id="schemafetchnomineerequest"></a>
<a id="tocSfetchnomineescmd"></a>
<a id="tocsfetchnomineescmd"></a>

```json
{
  "partyId": 1,
  "nomineeId": 0
}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer|false|none|none|
|nomineeId|integer|false|none|none|

<h2 id="tocS_NomineeResponse">NomineeResponse</h2>
<!-- backwards compatibility -->
<a id="schemanomineeresponse"></a>
<a id="schema_NomineeResponse"></a>
<a id="tocSnomineeresponse"></a>
<a id="tocsnomineeresponse"></a>

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "salutation": "Mr.",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "relation": "FATHER",
  "nationalId": "string"
}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|Nominee|[[Nominee](#schemanominees)]|false|none|none|

<h2 id="tocS_PartyNomineesCmd">NomineeFetchResponse</h2>
<!-- backwards compatibility -->
<a id="schemanomineefetchresponse"></a>
<a id="schema_NomineeFetchResponse"></a>
<a id="tocSnomineefetchresponse"></a>
<a id="tocsnomineefetchresponse"></a>

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "salutation": "Mr.",
  "firstName": "string",
  "middleName": "string",
  "lastName": "string",
  "relation": "FATHER",
  "nationalId": "string"
}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|Nominee|[[Nominee](#schemanominees)]|false|none|none|

<h2 id="tocS_NomineeMappingRequest">NomineeMappingRequest</h2>
<!--backwards compatibility-->
<a id="schemanomineemappingrequest"></a>
<a id="schema_NomineeMappingRequest"></a>
<a id="tocSnomineemappingrequest"></a>
<a id="tocsnomineemappingrequest"></a>

```json
{
  "partyId": 1,
  "nomineeId": 0,
  "accountNumber": "string"
}
```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|partyId|integer(int32)|false|none|none|
|nomineeId|integer(int32)|false|none|none|
|accountNumber|string|false|none|none|

<h2 id="tocS_ResponseApi">ResponseApi</h2>
<!-- backwards compatibility -->
<a id="schemaresponseapi"></a>
<a id="schema_Responseapi"></a>
<a id="tocSresponseapi"></a>
<a id="tocsresponseapi"></a>

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  }
}
```
###Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|code|string|false|none|none|
|message|string|false|none|none|

<h2 id="tocS_ResponseApi">SuccessResponseApi</h2>
<!-- backwards compatibility -->
<a id="schemasuccessresponseapi"></a>
<a id="schema_Succesresponseapi"></a>
<a id="tocSsuccesresponseapi"></a>
<a id="tocssuccesresponseapi"></a>

```json
{
  "meta": {
    "requestId": "string"
  },
  "status": {
    "code": "string",
    "message": "string"
  },
  "data": { }
}
```
###Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|code|string|false|none|none|
|message|string|false|none|none|
