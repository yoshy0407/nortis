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

CREATE TABLE IF NOT EXISTS RECEIVE_EVENT (
	EVENT_ID varchar(36) not null PRIMARY KEY,
	TENANT_ID varchar(10) not null,
	ENDPOINT_ID varchar(10) not null,
	OCCURED_ON timestamp not null,
	SUBSCRIBED char(1) not null,
	PARAMETER_JSON text,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;
CREATE TABLE IF NOT EXISTS SEND_MESSAGE (
	EVENT_ID varchar(36) not null,
	TEXT_TYPE varchar(5) not null,
	SUBJECT varchar(100) not null,
	BODY text not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	PRIMARY KEY(EVENT_ID, TEXT_TYPE)	
)
;

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

CREATE TABLE IF NOT EXISTS TENANT (
	TENANT_ID varchar(10) PRIMARY KEY,
	TENANT_NAME varchar(50) not null,
	TENANT_IDENTIFIER varchar(20) not null UNIQUE,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
)
;

CREATE SEQUENCE IF NOT EXISTS TENANT_SEQ
;

CREATE TABLE IF NOT EXISTS TENANT_ROLE (
	TENANT_ID varchar(10) NOT NULL,
	ROLE_ID varchar(5) NOT NULL,
	ROLE_NAME varchar(20) NOT NULL,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint,
	PRIMARY KEY(TENANT_ID, ROLE_ID)
)
;

CREATE SEQUENCE IF NOT EXISTS TENANT_ROLE_SEQ
;

CREATE TABLE IF NOT EXISTS ROLE_OPERATION (
	ROLE_ID varchar(5) NOT NULL,
	OPERATION_ID varchar(5) NOT NULL,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	PRIMARY KEY(ROLE_ID, OPERATION_ID)
)
;
