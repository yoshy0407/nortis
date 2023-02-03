CREATE TABLE IF NOT EXISTS SUSER (
	USER_ID varchar(10) PRIMARY KEY,
	USERNAME varchar(50),
	ENCODED_PASSWORD varchar(100),
	ADMIN_FLG char(1),
	LOGIN_FLG char(1),
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;

CREATE TABLE IF NOT EXISTS TENANT_USER (
	USER_ID varchar(10),
	TENANT_ID varchar(10),
	PRIMARY KEY(USER_ID, TENANT_ID)
)
;