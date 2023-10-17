select
	/*%expand */*
from
	RECEIVE_EVENT
where
	SUBSCRIBED = '0'
	AND ENDPOINT_ID = /* endpointId */''
order by
	OCCURED_ON asc