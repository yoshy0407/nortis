select
	/*%expand */*
from
	ENDPOINT
where
	TENANT_ID = /* tenantId */''
	AND ENDPOINT_ID = /* endpointId */''