select
	/*%expand */*
from
	AUTHENTICATION
where
	TENANT_ID = /* tenantId */''