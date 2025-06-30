package com.example.shedlockdemo;

import com.example.shedlockdemo.service.EODService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class ShedLockDemoApplicationTests {

    @Autowired
    private EODService eodService;

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }

    @Test
    void eodServiceIsAutowired() {
        assertNotNull(eodService, "EODService should be autowired");
    }

    @Test
    void canCheckEODStatus() {
        // Test that we can call the EOD status check method
        boolean status = eodService.isEODMarkedForToday();
        // We don't assert the result as it depends on the current state
        // Just verify the method doesn't throw an exception
    }
} 