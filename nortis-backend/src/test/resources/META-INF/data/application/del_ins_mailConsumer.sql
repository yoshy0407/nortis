DELETE FROM MAIL_CONSUMER
;
INSERT INTO MAIL_CONSUMER 
	(CONSUMER_ID, ENDPOINT_ID, MAIL_ADDRESS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('68E75233-7C82-40A6-A34D-CD5FD858EFA6', 'TEST1', 'hoge@example.com', 'TEST_ID', to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), null, null, 1)
;
INSERT INTO MAIL_CONSUMER 
	(CONSUMER_ID, ENDPOINT_ID, MAIL_ADDRESS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('7E7BC9C2-133A-49E5-9B34-F52078D6056D', 'TEST2', 'hoge@example.com', 'TEST_ID', to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), null, null, 1)
;
INSERT INTO MAIL_CONSUMER 
	(CONSUMER_ID, ENDPOINT_ID, MAIL_ADDRESS, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('3AC62B43-D866-416C-B205-7EC601104F4A', 'TEST1', 'hoge@example.com', 'TEST_ID', to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), null, null, 1)
;