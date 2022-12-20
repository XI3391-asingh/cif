INSERT INTO
	system_detail(system_code, system_type, description)
VALUES
	(
		'TM', 'INTEGRATION', 'TM Integration System'
	)
;
INSERT INTO
	system_detail(system_code, system_type, description)
VALUES
	(
		'Onboarding', 'SOURCE', 'Onboarding Source System'
	)
;
INSERT INTO
	change_config( system_id, config_fields, api_details, is_active)
VALUES
	(
		1, '{"firstName", "middelName","lastName","gender","nationality","dateOfBirth","primaryEmail","countryOfBirth"}', '{"baseUrl":"https:\/\/core-api-demo.xerus.tmachine.io\/v1\/","create":{"endPoint":"customers","authToken":"A0002298703723329582277!I1Ju\/1ptLdYfZxH3Mnm+Y2pBmUs1tU4QlQU5Xxb0CtNhxw\/NDK\/f0cbYnXTcd0P6otLIuMFwnXvuWQmeY00zSarvPE8="},"update":{"endPoint":"customers\/{customerId}","authToken":"A0002298703723329582277!I1Ju\/1ptLdYfZxH3Mnm+Y2pBmUs1tU4QlQU5Xxb0CtNhxw\/NDK\/f0cbYnXTcd0P6otLIuMFwnXvuWQmeY00zSarvPE8="}}'::jsonb, TRUE
	)
;

INSERT INTO
	adapter_config( source_system, integration_system, is_create, is_update)
VALUES
	(
		2, 1, TRUE, TRUE
	)
;
