package com.runtracker.producer;

import com.runtracker.model.RunEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class MockRunSimulator {

    @Autowired
    private GpsProducerService gpsProducerService;

    // Track distance for each user separately
    private Map<String, Double> distanceMap = new HashMap<>();

    @Scheduled(fixedRate = 5000)
    public void simulateRun() {

        // Simulate 3 users running simultaneously
        simulateUser("user_001", "run_001", 12.9716, 77.5946);
        simulateUser("user_002", "run_002", 12.9352, 77.6245);
        simulateUser("user_003", "run_003", 12.9279, 77.6271);
    }

    private void simulateUser(String userId, String runId,
                              double baseLat, double baseLng) {

        // Get current distance for this user (default 0.0)
        double currentDistance = distanceMap.getOrDefault(userId, 0.0);

        // Increase distance
        currentDistance += 0.035;

        // Save updated distance
        distanceMap.put(userId, currentDistance);

        // Create RunEvent
        RunEvent event = new RunEvent(
                userId,
                runId,
                baseLat + (Math.random() * 0.001),
                baseLng + (Math.random() * 0.001),
                7.5 + (Math.random() * 1.0),
                currentDistance,
                System.currentTimeMillis()
        );

        gpsProducerService.sendEvent(event);
    }
}