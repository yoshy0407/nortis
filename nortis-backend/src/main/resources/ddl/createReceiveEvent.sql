CREATE TABLE IF NOT EXISTS RECEIVE_EVENT (
	EVENT_ID varchar(36) not null PRIMARY KEY,
	TENANT_ID varchar(10) not null,
	ENDPOINT_ID varchar(10) not null,
	OCCURED_ON timestamp not null,
	SUBSCRIBED char(1) not null,
	TEMPLATE_PARAMETER text not null,
	UPDATE_DT timestamp
)
;