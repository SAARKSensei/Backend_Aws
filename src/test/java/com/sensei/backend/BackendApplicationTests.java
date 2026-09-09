package com.sensei.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "razorpay.key.id=dummy_test_key_id",
    "razorpay.key.secret=dummy_test_key_secret",
    "JWT_SECRET=dummy_test_jwt_secret_must_be_256bits_long_minimum",
    "GOOGLE_CLIENT_ID=dummy_google_client_id"
})
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
