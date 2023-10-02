CREATE TABLE IF NOT EXISTS ENDPOINT (
	TENANT_ID varchar(10) not null,
	ENDPOINT_ID varchar(10) not null,
	ENDPOINT_IDENTIFIER varchar(20) not null unique,
	ENDPOINT_NAME varchar(50) not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint,
	PRIMARY KEY(TENANT_ID, ENDPOINT_ID)
)
;

CREATE TABLE IF NOT EXISTS MESSAGE_TEMPLATE (
	ENDPOINT_ID varchar(10) not null,
	TEXT_TYPE varchar(5) not null,
	SUBJECT_TEMPLATE varchar(100) not null,
	BODY_TEMPLATE text not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint,
	PRIMARY KEY(ENDPOINT_ID, TEXT_TYPE)	
)
;

CREATE SEQUENCE IF NOT EXISTS ENDPOINT_SEQ
;
