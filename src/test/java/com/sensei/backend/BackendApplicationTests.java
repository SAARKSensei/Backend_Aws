package com.sensei.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "JWT_SECRET=test_secret_key_with_32_chars_minimum")
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
