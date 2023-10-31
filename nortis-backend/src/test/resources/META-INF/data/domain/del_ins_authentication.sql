DELETE FROM AUTHENTICATION;
INSERT INTO AUTHENTICATION
	(API_KEY, TENANT_ID, USER_ID, LAST_ACCESS_DATETIME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('APIKEYTENANT1', '1000000001', null, to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), 'TEST', CURRENT_TIMESTAMP, null, null, 1)
;
INSERT INTO AUTHENTICATION
	(API_KEY, TENANT_ID, USER_ID, LAST_ACCESS_DATETIME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('APIKEYTENANT2', '1000000002', null, to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), 'TEST', CURRENT_TIMESTAMP, null, null, 1)
;
INSERT INTO AUTHENTICATION
	(API_KEY, TENANT_ID, USER_ID, LAST_ACCESS_DATETIME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('APIKEYTENANT3', '1000000003', null, to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), 'TEST', CURRENT_TIMESTAMP, null, null, 1)
;
INSERT INTO AUTHENTICATION
	(API_KEY, TENANT_ID, USER_ID, LAST_ACCESS_DATETIME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('APIKEYUSER1', null, '0000000001', to_timestamp('2022-01-05T12:00:00','YYYY-MM-DDTHH24:MI:SS'), 'TEST', CURRENT_TIMESTAMP, null, null, 1)
;
INSERT INTO AUTHENTICATION
	(API_KEY, TENANT_ID, USER_ID, LAST_ACCESS_DATETIME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('APIKEYUSER2', null, '0000000002', to_timestamp('2022-01-05T12:15:00','YYYY-MM-DDTHH24:MI:SS'), 'TEST', CURRENT_TIMESTAMP, null, null, 1)
;
INSERT INTO AUTHENTICATION
	(API_KEY, TENANT_ID, USER_ID, LAST_ACCESS_DATETIME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('APIKEYUSER3', null, '0000000003', current_timestamp, 'TEST', CURRENT_TIMESTAMP, null, null, 1)
;