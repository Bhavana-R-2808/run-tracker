package com.runtracker.consumer;

import com.runtracker.entity.RunRecord;
import com.runtracker.model.RunEvent;
import com.runtracker.repository.RunRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunHistoryConsumerTest {

    @Mock
    private RunRepository runRepository;

    @InjectMocks
    private RunHistoryConsumer runHistoryConsumer;

    @Test
    void shouldSaveRunRecordWhenEventReceived() {
        // Arrange — create a fake RunEvent
        RunEvent event = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                7.8, 0.035,
                System.currentTimeMillis()
        );

        // Act — call the consumer
        runHistoryConsumer.consumeRunEvent(event);

        // Assert — verify repository.save() was called
        verify(runRepository, times(1)).save(any(RunRecord.class));
    }

    @Test
    void shouldCreateRunRecordWithCorrectUserId() {
        // Arrange
        RunEvent event = new RunEvent(
                "user_002", "run_002",
                12.9352, 77.6245,
                8.1, 0.070,
                System.currentTimeMillis()
        );

        // Capture what was saved to repository
        ArgumentCaptor<RunRecord> captor =
                ArgumentCaptor.forClass(RunRecord.class);

        // Act
        runHistoryConsumer.consumeRunEvent(event);

        // Assert — capture the saved RunRecord
        verify(runRepository).save(captor.capture());
        RunRecord savedRecord = captor.getValue();

        assertEquals("user_002", savedRecord.getUserId());
        assertEquals("run_002", savedRecord.getRunId());
        assertEquals(8.1, savedRecord.getPace());
        assertEquals(0.070, savedRecord.getDistanceKm());
    }

    @Test
    void shouldSaveCorrectPaceToDatabase() {
        // Arrange
        double expectedPace = 7.5;
        RunEvent event = new RunEvent(
                "user_003", "run_003",
                12.9279, 77.6271,
                expectedPace, 0.035,
                System.currentTimeMillis()
        );

        ArgumentCaptor<RunRecord> captor =
                ArgumentCaptor.forClass(RunRecord.class);

        // Act
        runHistoryConsumer.consumeRunEvent(event);

        // Assert
        verify(runRepository).save(captor.capture());
        assertEquals(expectedPace,
                captor.getValue().getPace());
    }

    @Test
    void shouldSaveOncePerEvent() {
        // Arrange
        RunEvent event = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                8.0, 0.035,
                System.currentTimeMillis()
        );

        // Act
        runHistoryConsumer.consumeRunEvent(event);

        // Assert — save called exactly once, not twice
        verify(runRepository, times(1))
                .save(any(RunRecord.class));
    }
}
