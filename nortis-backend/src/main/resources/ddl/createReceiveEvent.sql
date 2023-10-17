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
