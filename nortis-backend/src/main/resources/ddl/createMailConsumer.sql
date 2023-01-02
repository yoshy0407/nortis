CREATE TABLE IF NOT EXISTS MAIL_CONSUMER (
	CONSUMER_ID varchar(36) PRIMARY KEY,
	ENDPOINT_ID varchar(10) not null,
	MAIL_ADDRESS varchar(319) not null,
	CREATE_ID varchar(10) not null,
	CREATE_DT timestamp not null,
	UPDATE_ID varchar(10),
	UPDATE_DT timestamp,
	VERSION bigint
);