select
	/*%expand */*
from
	RECEIVE_EVENT
where
	SUBSCRIBED = 'FALSE'
order by
	OCCURED_ON asc