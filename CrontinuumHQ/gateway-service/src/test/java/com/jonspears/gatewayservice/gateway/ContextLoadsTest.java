package com.jonspears.gatewayservice.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContextLoadsTest {
    @Test
    void contextLoads() {
        assertThat(true).isTrue(); // Sanity check
    }
}
