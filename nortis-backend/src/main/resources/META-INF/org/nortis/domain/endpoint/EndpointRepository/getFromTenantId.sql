select
	/*%expand */*
from
	ENDPOINT
where
	TENANT_ID = /* tenantId */''