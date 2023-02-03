select
	/*%expand */*
from
	AUTHENTICATION
where
	USER_ID is not null
order by
	API_KEY