
<a name="paths"></a>
## Resources

<a name="badge-rules_resource"></a>
### Badge-rules
Handles CRUD operations on badge rules


<a name="createusingpost"></a>
#### Creates a new badge-rule in the domain
```
POST /api/badge-rules
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|body|[BadgeRuleUpdate](#badgeruleupdate)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[BadgeRuleDetail](#badgeruledetail)|
|**201**|Created|[BadgeRuleDetail](#badgeruledetail)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error creating badge-rule in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="indexusingget"></a>
#### Retreives all badge-rules from the domain
```
GET /api/badge-rules
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|< [BadgeRuleSummary](#badgerulesummary) > array|
|**201**|Created|< [BadgeRuleSummary](#badgerulesummary) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving badge-rules from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="showusingget"></a>
#### Retrieves a particular badge-rule from the domain
```
GET /api/badge-rules/{id}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[BadgeRuleDetail](#badgeruledetail)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving the badge-rule from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput"></a>
#### Updates a particular badge-rule from the domain
```
PUT /api/badge-rules/{id}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|
|**Body**|**body**  <br>*required*|body|[BadgeRuleUpdate](#badgeruleupdate)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[BadgeRuleDetail](#badgeruledetail)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating the badge-rule from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete"></a>
#### Deletes a particular badge-rule from the domain
```
DELETE /api/badge-rules/{id}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the badge-rule from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="evalusingget"></a>
#### Evaluates a badge-rules's script againts the specified user's counters (debug method, does not trigger grants)
```
GET /api/badge-rules/{id}/evaluate
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|
|**Query**|**user**  <br>*required*|pid|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[EvaluationResult](#evaluationresult)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error evaluating the rule|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="badge-types_resource"></a>
### Badge-types
Handles CRUD operations on badges-types


<a name="createusingpost_1"></a>
#### Creates a new badge-type in the domain
```
POST /api/badge-types
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|body|[BadgeTypeDetail](#badgetypedetail)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**201**|Created|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error creating the badge-type in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="indexusingget_1"></a>
#### Retrieves all badge-types from the domain
```
GET /api/badge-types
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|< [BadgeTypeSummary](#badgetypesummary) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving badge-types from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="showusingget_1"></a>
#### Retrieves a particular badge-type from the domain
```
GET /api/badge-types/{key}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**key**  <br>*required*|key|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[BadgeTypeDetail](#badgetypedetail)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving the badge-type from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput_1"></a>
#### Updates a particular badge-type from the domain
```
PUT /api/badge-types/{key}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**key**  <br>*required*|key|string|
|**Body**|**body**  <br>*required*|body|[BadgeTypeDetail](#badgetypedetail)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[BadgeTypeSummary](#badgetypesummary)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating the badge-type from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete_1"></a>
#### Deletes a particular badge-type from the domain
```
DELETE /api/badge-types/{key}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**key**  <br>*required*|key|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the badge-type from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="counters_resource"></a>
### Counters
Handles CRUD operations on counters


<a name="createusingpost_2"></a>
#### Creates a new counter in the domain
```
POST /api/counters
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|body|[CounterUpdateView](#counterupdateview)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**201**|Created|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error creating counter in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="indexusingget_2"></a>
#### Retrieves all the counters from the domain
```
GET /api/counters
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|< [CounterSummary](#countersummary) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving counters from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="showusingget_2"></a>
#### Retrieves a particular counter from the domain
```
GET /api/counters/{name}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**name**  <br>*required*|name|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[CounterSummary](#countersummary)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving the counters from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput_2"></a>
#### Updates a particular counter from the domain
```
PUT /api/counters/{name}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**name**  <br>*required*|name|string|
|**Body**|**body**  <br>*required*|body|[CounterUpdateView](#counterupdateview)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[CounterSummary](#countersummary)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating the counter from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete_2"></a>
#### Deletes a particular counter from the domain
```
DELETE /api/counters/{name}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**name**  <br>*required*|name|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the counter from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="domain_resource"></a>
### Domain
Handle CRUD operation on domains


<a name="showusingget_3"></a>
#### Gets info about the current domain
```
GET /api/domain
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[DomainSummary](#domainsummary)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving information from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput_3"></a>
#### Updates the current domain
```
PUT /api/domain
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**input**  <br>*required*|input|[DomainUpdate](#domainupdate)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[DomainSummary](#domainsummary)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating information in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete_3"></a>
#### Destroy the current domain
```
DELETE /api/domain
```


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|No Content|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="event-rules_resource"></a>
### Event-rules
Handles CRUD operations on event-rules


<a name="createusingpost_3"></a>
#### Creates a new event-rule in the domain
```
POST /api/event-rules
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|body|[EventRuleSummary](#eventrulesummary)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[EventRuleOutputView](#eventruleoutputview)|
|**201**|Created|[EventRuleOutputView](#eventruleoutputview)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error creating event-rule in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="indexusingget_3"></a>
#### Retreives all event-rules from the domain
```
GET /api/event-rules
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|
|**Query**|**type**  <br>*optional*|type|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|< [EventRuleDetail](#eventruledetail) > array|
|**201**|Created|< [EventRuleDetail](#eventruledetail) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving event-rules from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="showusingget_4"></a>
#### Retrieves a particular event-rule from the domain
```
GET /api/event-rules/{id}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving the event-rule from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput_4"></a>
#### Updates a particular event-rule from the domain
```
PUT /api/event-rules/{id}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|
|**Body**|**body**  <br>*required*|body|[EventRuleSummary](#eventrulesummary)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[EventRuleOutputView](#eventruleoutputview)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating the event-rule from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete_4"></a>
#### Deletes a particular event-rule from the domain
```
DELETE /api/event-rules/{id}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**id**  <br>*required*|id|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the event-rule from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="events_resource"></a>
### Events
Handles events from users


<a name="postusingpost"></a>
#### Publishes an event to a user
```
POST /api/events
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**input**  <br>*required*|input|[EventSubmit](#eventsubmit)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[EventResult](#eventresult)|
|**201**|Created|[EventResult](#eventresult)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error publishing event in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="getusingget"></a>
#### Obtain events for a user
```
GET /api/events
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|
|**Query**|**user**  <br>*required*|userId|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[EventInfos](#eventinfos)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error obtaining event from a user in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="metrics_resource"></a>
### Metrics
Handles CRUD operations on metrics


<a name="createusingpost_4"></a>
#### Creates a new metric in the domain
```
POST /api/counters/{counterName}/metrics
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**counterName**  <br>*required*|counterName|string|
|**Body**|**body**  <br>*required*|body|[MetricUpdate](#metricupdate)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**201**|Created|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|
|**500**|Error creating the metric in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="indexusingget_4"></a>
#### Retrieves a particular metric from the domain
```
GET /api/counters/{counterName}/metrics
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**counterName**  <br>*required*|counterName|string|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|< [MetricSummary](#metricsummary) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving metric from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="showusingget_5"></a>
#### Retrieve a particular metric from the domain
```
GET /api/counters/{counterName}/metrics/{metricName}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**counterName**  <br>*required*|counterName|string|
|**Path**|**metricName**  <br>*required*|metricName|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[MetricSummary](#metricsummary)|
|**201**|Created|[MetricSummary](#metricsummary)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving the metric from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput_5"></a>
#### Update a particular metric from the domain
```
PUT /api/counters/{counterName}/metrics/{metricName}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**counterName**  <br>*required*|counterName|string|
|**Path**|**metricName**  <br>*required*|metricName|string|
|**Body**|**body**  <br>*required*|body|[MetricUpdate](#metricupdate)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[MetricSummary](#metricsummary)|
|**201**|Created|[MetricSummary](#metricsummary)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating the metric from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete_5"></a>
#### Delete a particular metric from the domain
```
DELETE /api/counters/{counterName}/metrics/{metricName}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**counterName**  <br>*required*|counterName|string|
|**Path**|**metricName**  <br>*required*|metricName|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the metric from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="register_resource"></a>
### Register
Handles domain registration


<a name="registerusingpost"></a>
#### Registers a new Domain
```
POST /register
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|body|[DomainUpdate](#domainupdate)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|[DomainSummary](#domainsummary)|
|**201**|Created|[DomainSummary](#domainsummary)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not Found|No Content|
|**500**|Error during creation|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="users_resource"></a>
### Users
Handles CRUD operations on users


<a name="createusingpost_5"></a>
#### Creates a new user in the domain
```
POST /api/users
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Body**|**body**  <br>*required*|body|[UserSummary](#usersummary)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|OK|object|
|**201**|Created|object|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error creating user in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="indexusingget_5"></a>
#### Retrieves all users from the domain
```
GET /api/users
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|< [UserSummary](#usersummary) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving users from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="showusingget_6"></a>
#### Retrieves a particular user from the domain
```
GET /api/users/{pid}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pid**  <br>*required*|pid|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[UserSummary](#usersummary)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving the user from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="updateusingput_6"></a>
#### Updates a particular user in the domain
```
PUT /api/users/{pid}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pid**  <br>*required*|pid|string|
|**Body**|**body**  <br>*required*|body|[UserSummary](#usersummary)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[UserSummary](#usersummary)|
|**201**|Created|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error updating the user in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `application/json`
* `*/*`


<a name="deleteusingdelete_6"></a>
#### Deletes a particular user from the domain
```
DELETE /api/users/{pid}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pid**  <br>*required*|pid|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|object|
|**204**|No Content|No Content|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error deleting the user from the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="badgesusingget"></a>
#### Retrieves all badges from a particular user in the domain
```
GET /api/users/{pid}/badges
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**pid**  <br>*required*|pid|string|
|**Query**|**page**  <br>*optional*|page|integer(int64)|
|**Query**|**pageSize**  <br>*optional*|pageSize|integer(int64)|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|< [BadgeSummary](#badgesummary) > array|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving badges from a particular user in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`


<a name="counterusingget"></a>
#### Retrieves the value of a counter from a particular user in the domain
```
GET /api/users/{pid}/counter/{cid}
```


##### Parameters

|Type|Name|Description|Schema|
|---|---|---|---|
|**Path**|**cid**  <br>*required*|cid|string|
|**Path**|**pid**  <br>*required*|pid|string|


##### Responses

|HTTP Code|Description|Schema|
|---|---|---|
|**200**|Ok|[CounterAggregate](#counteraggregate)|
|**401**|Unauthorized|No Content|
|**403**|Forbidden|No Content|
|**404**|Not found|No Content|
|**500**|Error retrieving badges from a particular user in the domain|No Content|


##### Consumes

* `application/json`


##### Produces

* `*/*`



