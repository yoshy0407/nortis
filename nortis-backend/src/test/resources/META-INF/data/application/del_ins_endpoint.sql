DELETE FROM ENDPOINT;
INSERT INTO ENDPOINT
	(TENANT_ID, ENDPOINT_ID, ENDPOINT_NAME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('TEST1', 'ENDPOINT1', 'テストエンドポイント１', 'TEST_ID', to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), null, null, 1);
INSERT INTO ENDPOINT
	(TENANT_ID, ENDPOINT_ID, ENDPOINT_NAME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('TEST2', 'ENDPOINT2', 'テストエンドポイント２', 'TEST_ID', to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), null, null, 1);
INSERT INTO ENDPOINT
	(TENANT_ID, ENDPOINT_ID, ENDPOINT_NAME, CREATE_ID, CREATE_DT, UPDATE_ID, UPDATE_DT, VERSION)
VALUES
	 ('TEST3', 'ENDPOINT3', 'テストエンドポイント３','TEST_ID', to_timestamp('2022-01-05T12:56:34','YYYY-MM-DDTHH24:MI:SS'), null, null, 1);