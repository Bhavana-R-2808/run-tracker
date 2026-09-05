package com.runtracker.controller;

import com.runtracker.entity.RunRecord;
import com.runtracker.repository.RunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RunController {

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Endpoint 1 — Get leaderboard from Redis
    @GetMapping("/leaderboard/{routeId}")
    public Map<String, Object> getLeaderboard(@PathVariable String routeId) {
        String key = "leaderboard:" + routeId;
        Set<String> leaderboard = redisTemplate
                .opsForZSet()
                .range(key, 0, -1);

        Map<String, Object> response = new HashMap<>();
        response.put("routeId", routeId);
        response.put("leaderboard", leaderboard);
        return response;
    }

    // Endpoint 2 — Get run history from MySQL
    @GetMapping("/runs/{userId}/history")
    public Map<String, Object> getRunHistory(@PathVariable String userId) {
        List<RunRecord> records = runRepository.findByUserId(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("totalRuns", records.size());
        response.put("history", records);
        return response;
    }

    // Endpoint 3 — Get run stats from MySQL
    @GetMapping("/runs/{userId}/stats")
    public Map<String, Object> getRunStats(@PathVariable String userId) {
        List<RunRecord> records = runRepository.findByUserId(userId);

        if (records.isEmpty()) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("message", "No runs found for user: " + userId);
            return empty;
        }

        // Calculate stats
        double totalDistance = records.stream()
                .mapToDouble(RunRecord::getDistanceKm)
                .max()
                .orElse(0.0);

        double avgPace = records.stream()
                .mapToDouble(RunRecord::getPace)
                .average()
                .orElse(0.0);

        double bestPace = records.stream()
                .mapToDouble(RunRecord::getPace)
                .min()
                .orElse(0.0);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("totalEvents", records.size());
        response.put("totalDistanceKm", String.format("%.2f", totalDistance));
        response.put("averagePace", String.format("%.2f", avgPace));
        response.put("bestPace", String.format("%.2f", bestPace));
        return response;
    }

    // Endpoint 4 — Start a new run
    @PostMapping("/runs/start")
    public Map<String, Object> startRun() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Run started! GPS events flowing to Kafka.");
        response.put("userId", "user_001");
        response.put("status", "RUNNING");
        return response;
    }
}