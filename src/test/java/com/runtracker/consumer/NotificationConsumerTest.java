package com.runtracker.consumer;

import com.runtracker.model.RunEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @BeforeEach
    void setUp() {
        notificationConsumer = new NotificationConsumer();
    }

    @Test
    void shouldDetectPersonalBestOnFirstRun() {
        RunEvent event = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                7.8, 0.035,
                System.currentTimeMillis()
        );

        assertDoesNotThrow(() ->
                notificationConsumer.consumeRunEvent(event));
    }

    @Test
    void shouldDetectOneKmMilestone() {
        RunEvent event = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                8.0, 1.0,
                System.currentTimeMillis()
        );

        assertDoesNotThrow(() ->
                notificationConsumer.consumeRunEvent(event));
    }

    @Test
    void shouldDetectFiveKmMilestone() {
        RunEvent event = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                8.0, 5.0,
                System.currentTimeMillis()
        );

        assertDoesNotThrow(() ->
                notificationConsumer.consumeRunEvent(event));
    }

    @Test
    void shouldHandleMultipleEventsWithoutError() {
        for (int i = 1; i <= 5; i++) {
            RunEvent event = new RunEvent(
                    "user_001", "run_001",
                    12.9716, 77.5946,
                    7.5 + (i * 0.1), i * 0.035,
                    System.currentTimeMillis()
            );
            assertDoesNotThrow(() ->
                    notificationConsumer.consumeRunEvent(event));
        }
    }
}