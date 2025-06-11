package com.jonspears.schedulerservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SchedulerServiceApplicationTests {

	@Test
	void contextLoads() {
		assertThat(true).isTrue(); // Sanity check
	}

}
