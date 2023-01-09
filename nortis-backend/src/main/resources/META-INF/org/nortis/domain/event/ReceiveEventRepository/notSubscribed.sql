select
	/*%expand */*
from
	RECEIVE_EVENT
where
	SUBSCRIBED = '0'
order by
	OCCURED_ON asc