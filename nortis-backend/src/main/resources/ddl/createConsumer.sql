CREATE TABLE IF NOT EXISTS CONSUMER (
	TENANT_ID varchar(10) not null,
	CONSUMER_ID varchar(10) not null,
	CONSUMER_NAME varchar(100) not null,
	CONSUMER_TYPE_CODE varchar(20) not null,
	TEXT_TYPE varchar(5) not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint,
	PRIMARY KEY (TENANT_ID, CONSUMER_ID)
);

CREATE SEQUENCE IF NOT EXISTS CONSUMER_SEQ
;

CREATE TABLE IF NOT EXISTS CONSUMER_PARAMETER (
	CONSUMER_ID varchar(10) not null,
	PARAMETER_NAME varchar(20) not null,
	PARAMETER_VALUE text,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint,
	PRIMARY KEY(CONSUMER_ID, PARAMETER_NAME)
);

CREATE TABLE IF NOT EXISTS CONSUMER_SUBSCRIBE (
	CONSUMER_ID varchar(10) not null,
	ENDPOINT_ID varchar(10) not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	PRIMARY KEY(CONSUMER_ID, ENDPOINT_ID)	
);
