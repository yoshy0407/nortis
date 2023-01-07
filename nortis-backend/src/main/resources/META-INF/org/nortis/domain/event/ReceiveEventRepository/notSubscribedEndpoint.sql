select
	/*%expand */*
from
	RECEIVE_EVENT
where
	SUBSCRIBED = 'FALSE'
	AND ENDPOINT_ID = /* endpointId */''
order by
	OCCURED_ON asc