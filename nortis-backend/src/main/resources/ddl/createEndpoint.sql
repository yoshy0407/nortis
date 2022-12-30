CREATE TABLE IF NOT EXISTS ENDPOINT (
	TENANT_ID varchar(10) not null,
	ENDPOINT_ID varchar(10) not null,
	ENDPOINT_NAME varchar(50) not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;