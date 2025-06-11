package com.jonspears.gatewayservice;

import com.jonspears.gatewayservice.gateway.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {GatewayServiceApplication.class, TestConfig.class})
class GatewayServiceApplicationTests {

	@Test
	void contextLoads() {

		assertTrue(true); // Sanity check
	}

}
