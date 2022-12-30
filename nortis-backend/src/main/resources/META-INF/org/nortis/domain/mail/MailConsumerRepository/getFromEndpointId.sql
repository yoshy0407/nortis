SELECT
	/*%expand */*
FROM
	MAIL_CONSUMER
where
	ENDPOINT_ID = /* endpointId */''
order by
	CONSUMER_ID