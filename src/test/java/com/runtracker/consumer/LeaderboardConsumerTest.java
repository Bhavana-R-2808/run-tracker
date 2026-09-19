package com.runtracker.consumer;

import com.runtracker.model.RunEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardConsumerTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private LeaderboardConsumer leaderboardConsumer;

    @Test
    void shouldUpdateLeaderboardWhenEventReceived() {
        // Arrange
        RunEvent event = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                7.8, 0.035,
                System.currentTimeMillis()
        );

        when(redisTemplate.opsForZSet())
                .thenReturn(zSetOperations);

        // Act
        leaderboardConsumer.consumeRunEvent(event);

        // Assert — verify Redis was updated
        verify(zSetOperations, times(1))
                .add("leaderboard:route1", "user_001", 7.8);
    }

    @Test
    void shouldUpdateLeaderboardWithCorrectPace() {
        // Arrange
        double expectedPace = 8.2;
        RunEvent event = new RunEvent(
                "user_002", "run_002",
                12.9352, 77.6245,
                expectedPace, 0.070,
                System.currentTimeMillis()
        );

        when(redisTemplate.opsForZSet())
                .thenReturn(zSetOperations);

        // Act
        leaderboardConsumer.consumeRunEvent(event);

        // Assert
        verify(zSetOperations, times(1))
                .add("leaderboard:route1", "user_002", expectedPace);
    }

    @Test
    void shouldUpdateLeaderboardForMultipleUsers() {
        // Arrange
        when(redisTemplate.opsForZSet())
                .thenReturn(zSetOperations);

        RunEvent event1 = new RunEvent(
                "user_001", "run_001",
                12.9716, 77.5946,
                7.8, 0.035,
                System.currentTimeMillis()
        );

        RunEvent event2 = new RunEvent(
                "user_002", "run_002",
                12.9352, 77.6245,
                8.1, 0.035,
                System.currentTimeMillis()
        );

        // Act
        leaderboardConsumer.consumeRunEvent(event1);
        leaderboardConsumer.consumeRunEvent(event2);

        // Assert — both users updated in Redis
        verify(zSetOperations, times(1))
                .add("leaderboard:route1", "user_001", 7.8);
        verify(zSetOperations, times(1))
                .add("leaderboard:route1", "user_002", 8.1);
    }
}