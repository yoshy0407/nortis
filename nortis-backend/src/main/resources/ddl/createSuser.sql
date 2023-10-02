CREATE TABLE IF NOT EXISTS SUSER (
	USER_ID varchar(10) PRIMARY KEY,
	USERNAME varchar(50),
	LOGIN_ID varchar(30) UNIQUE,
	HASHED_PASSWORD varchar(100),
	LOGIN_FLG char(1),
	ADMIN_FLG char(1),
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;

CREATE TABLE IF NOT EXISTS USER_ROLE (
	USER_ID varchar(10),
	TENANT_ID varchar(10),
	ROLE_ID varchar(5) NOT NULL,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	PRIMARY KEY(USER_ID, TENANT_ID)
)
;

CREATE SEQUENCE IF NOT EXISTS USER_SEQ
;
