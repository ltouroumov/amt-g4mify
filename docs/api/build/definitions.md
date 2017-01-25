
<a name="definitions"></a>
## Definitions

<a name="badgeruledetail"></a>
### BadgeRuleDetail

|Name|Schema|
|---|---|
|**condition**  <br>*optional*|string|
|**depends**  <br>*optional*|< string > array|
|**grants**  <br>*optional*|[BadgeTypeSummary](#badgetypesummary)|
|**id**  <br>*optional*|integer(int64)|


<a name="badgerulesummary"></a>
### BadgeRuleSummary

|Name|Schema|
|---|---|
|**condition**  <br>*optional*|string|
|**grants**  <br>*optional*|string|
|**id**  <br>*optional*|integer(int64)|


<a name="badgeruleupdate"></a>
### BadgeRuleUpdate

|Name|Schema|
|---|---|
|**condition**  <br>*optional*|string|
|**grants**  <br>*optional*|string|


<a name="badgesummary"></a>
### BadgeSummary

|Name|Schema|
|---|---|
|**awarded**  <br>*optional*|integer(int64)|
|**level**  <br>*optional*|integer(int64)|
|**type**  <br>*optional*|[BadgeTypeSummary](#badgetypesummary)|


<a name="badgetypedetail"></a>
### BadgeTypeDetail

|Name|Schema|
|---|---|
|**color**  <br>*optional*|string|
|**image**  <br>*optional*|string|
|**isSingleton**  <br>*optional*|boolean|
|**key**  <br>*optional*|string|
|**name**  <br>*optional*|string|
|**previous**  <br>*optional*|string|


<a name="badgetypesummary"></a>
### BadgeTypeSummary

|Name|Schema|
|---|---|
|**color**  <br>*optional*|string|
|**image**  <br>*optional*|string|
|**key**  <br>*optional*|string|
|**name**  <br>*optional*|string|


<a name="counteraggregate"></a>
### CounterAggregate

|Name|Schema|
|---|---|
|**metrics**  <br>*optional*|< string, integer(int64) > map|
|**name**  <br>*optional*|string|


<a name="countersummary"></a>
### CounterSummary

|Name|Schema|
|---|---|
|**id**  <br>*optional*|integer(int64)|
|**metrics**  <br>*optional*|< [MetricSummary](#metricsummary) > array|
|**name**  <br>*optional*|string|


<a name="counterupdateview"></a>
### CounterUpdateView

|Name|Schema|
|---|---|
|**name**  <br>*optional*|string|


<a name="domainsummary"></a>
### DomainSummary

|Name|Schema|
|---|---|
|**id**  <br>*optional*|integer(int64)|
|**key**  <br>*optional*|string|
|**name**  <br>*optional*|string|


<a name="domainupdate"></a>
### DomainUpdate

|Name|Schema|
|---|---|
|**name**  <br>*optional*|string|


<a name="evaluationresult"></a>
### EvaluationResult

|Name|Schema|
|---|---|
|**result**  <br>*optional*|boolean|


<a name="eventdata"></a>
### EventData

|Name|Schema|
|---|---|
|**longValue**  <br>*optional*|integer(int64)|
|**stringValue**  <br>*optional*|string|


<a name="eventinfos"></a>
### EventInfos

|Name|Schema|
|---|---|
|**events**  <br>*optional*|< [EventSummary](#eventsummary) > array|
|**user**  <br>*optional*|[UserSummary](#usersummary)|


<a name="eventresult"></a>
### EventResult

|Name|Schema|
|---|---|
|**data**  <br>*optional*|< string, [EventData](#eventdata) > map|
|**id**  <br>*optional*|integer(int64)|
|**processed**  <br>*optional*|integer(int64)|
|**received**  <br>*optional*|integer(int64)|
|**type**  <br>*optional*|string|
|**user**  <br>*optional*|[UserSummary](#usersummary)|


<a name="eventruledetail"></a>
### EventRuleDetail

|Name|Schema|
|---|---|
|**id**  <br>*optional*|integer(int64)|
|**script**  <br>*optional*|string|
|**types**  <br>*optional*|< string > array|


<a name="eventruleoutputview"></a>
### EventRuleOutputView

|Name|Schema|
|---|---|
|**id**  <br>*optional*|integer(int64)|
|**script**  <br>*optional*|string|
|**types**  <br>*optional*|< string > array|


<a name="eventrulesummary"></a>
### EventRuleSummary

|Name|Schema|
|---|---|
|**script**  <br>*optional*|string|
|**types**  <br>*optional*|< string > array|


<a name="eventsubmit"></a>
### EventSubmit

|Name|Schema|
|---|---|
|**data**  <br>*optional*|< string, [EventData](#eventdata) > map|
|**type**  <br>*optional*|string|
|**user**  <br>*optional*|string|


<a name="eventsummary"></a>
### EventSummary

|Name|Schema|
|---|---|
|**data**  <br>*optional*|< string, [EventData](#eventdata) > map|
|**id**  <br>*optional*|integer(int64)|
|**processed**  <br>*optional*|integer(int64)|
|**received**  <br>*optional*|integer(int64)|
|**type**  <br>*optional*|string|


<a name="b33dbcce7b78c1f2ab2f97d32536c471"></a>
### Map«string,EventData»
*Type* : < string, [EventData](#eventdata) > map


<a name="06f215af1e7e4247dda2ff26bd1fc0fe"></a>
### Map«string,long»
*Type* : < string, integer(int64) > map


<a name="metricsummary"></a>
### MetricSummary

|Name|Schema|
|---|---|
|**duration**  <br>*optional*|integer(int32)|
|**id**  <br>*optional*|integer(int64)|
|**name**  <br>*optional*|string|


<a name="metricupdate"></a>
### MetricUpdate

|Name|Schema|
|---|---|
|**duration**  <br>*optional*|integer(int32)|
|**name**  <br>*optional*|string|


<a name="usersummary"></a>
### UserSummary

|Name|Schema|
|---|---|
|**profileId**  <br>*optional*|string|
|**profileUrl**  <br>*optional*|string|



