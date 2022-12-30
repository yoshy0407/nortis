CREATE TABLE IF NOT EXISTS TENANT (
	TENANT_ID varchar(10) PRIMARY KEY,
	TENANT_NAME varchar(50) not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;