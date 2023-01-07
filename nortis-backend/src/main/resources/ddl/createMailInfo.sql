CREATE TABLE IF NOT EXISTS MAIL_INFO (
	CONSUMER_ID varchar(36) not null,
	ORDER_NO integer not null,
	MAIL_ADDRESS varchar(319) not null,
	primary key(CONSUMER_ID, ORDER_NO)
);