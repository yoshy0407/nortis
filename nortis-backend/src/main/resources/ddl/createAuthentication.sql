CREATE TABLE IF NOT EXISTS AUTHENTICATION (
	API_KEY varchar(36) PRIMARY KEY,
	TENANT_ID varchar(10),
	USER_ID varchar(10),
	LAST_ACCESS_DATETIME timestamp,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;