package org.lbcc.bms.bms_monolith;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active=test")
class BmsMonolithApplicationTests {

	@Test
	void contextLoads() {
		// Test whether the application context is loading correctly
		assertTrue(true, "Context should load");
	}

}
