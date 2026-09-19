package com.runtracker.controller;

import com.runtracker.entity.RunRecord;
import com.runtracker.repository.RunRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RunControllerTest {

    @Mock
    private RunRepository runRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private RunController runController;

    @Test
    void shouldReturnLeaderboard() {
        // Arrange
        Set<String> leaderboard = new HashSet<>(
                Arrays.asList("user_001", "user_002", "user_003"));

        when(redisTemplate.opsForZSet())
                .thenReturn(zSetOperations);
        when(zSetOperations.range("leaderboard:route1", 0, -1))
                .thenReturn(leaderboard);

        // Act
        Map<String, Object> response =
                runController.getLeaderboard("route1");

        // Assert
        assertNotNull(response);
        assertEquals("route1", response.get("routeId"));
        assertNotNull(response.get("leaderboard"));
    }

    @Test
    void shouldReturnRunHistory() {
        // Arrange
        RunRecord record1 = new RunRecord(
                "user_001", "run_001",
                12.9716, 77.5946,
                7.8, 0.035,
                System.currentTimeMillis()
        );
        RunRecord record2 = new RunRecord(
                "user_001", "run_001",
                12.9716, 77.5946,
                8.1, 0.070,
                System.currentTimeMillis()
        );

        when(runRepository.findByUserId("user_001"))
                .thenReturn(Arrays.asList(record1, record2));

        // Act
        Map<String, Object> response =
                runController.getRunHistory("user_001");

        // Assert
        assertNotNull(response);
        assertEquals("user_001", response.get("userId"));
        assertEquals(2, response.get("totalRuns"));
    }

    @Test
    void shouldReturnRunStats() {
        // Arrange
        RunRecord record1 = new RunRecord(
                "user_001", "run_001",
                12.9716, 77.5946,
                7.8, 0.035,
                System.currentTimeMillis()
        );
        RunRecord record2 = new RunRecord(
                "user_001", "run_001",
                12.9716, 77.5946,
                8.2, 0.070,
                System.currentTimeMillis()
        );

        when(runRepository.findByUserId("user_001"))
                .thenReturn(Arrays.asList(record1, record2));

        // Act
        Map<String, Object> response =
                runController.getRunStats("user_001");

        // Assert
        assertNotNull(response);
        assertEquals("user_001", response.get("userId"));
        assertNotNull(response.get("averagePace"));
        assertNotNull(response.get("bestPace"));
        assertNotNull(response.get("totalDistanceKm"));
    }

    @Test
    void shouldReturnEmptyMessageForUnknownUser() {
        // Arrange
        when(runRepository.findByUserId("unknown_user"))
                .thenReturn(List.of());

        // Act
        Map<String, Object> response =
                runController.getRunStats("unknown_user");

        // Assert
        assertNotNull(response.get("message"));
        assertTrue(response.get("message")
                .toString().contains("unknown_user"));
    }

    @Test
    void shouldReturnRunningStatusOnStart() {
        // Act
        Map<String, Object> response =
                runController.startRun();

        // Assert
        assertEquals("RUNNING", response.get("status"));
        assertEquals("user_001", response.get("userId"));
        assertNotNull(response.get("message"));
    }
}