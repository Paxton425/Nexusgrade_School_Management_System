package com.nexusgrade.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "file:.env") // <-- Tells the test runner to load your .env file
class NexusGradeApplicationTests {

    @Test
    void contextLoads() {
    }

}
