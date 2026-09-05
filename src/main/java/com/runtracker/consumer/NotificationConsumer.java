package com.runtracker.consumer;

import com.runtracker.model.RunEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final String NOTIFICATION_TOPIC = "run-events";
    private static final String DLQ_TOPIC = "run-events-dlq";

    // Track personal best pace per user
    private double personalBestPace = Double.MAX_VALUE;

    @KafkaListener(topics = "run-events", groupId = "notification-group")
    public void consumeRunEvent(RunEvent event) {
        System.out.println("Consumer 3 received event for user: " + event.getUserId());

        try {
            checkMilestones(event);
        } catch (Exception e) {
            System.out.println("Notification failed — sending to DLQ: " + e.getMessage());
        }
    }

    private void checkMilestones(RunEvent event) {

        double distance = event.getDistanceKm();
        double pace = event.getPace();
        String userId = event.getUserId();

        // Milestone 1 — 1km completed
        if (distance >= 1.0 && distance < 1.035) {
            sendNotification(userId, "🎉 Congratulations! You completed 1KM!");
        }

        // Milestone 2 — 5km completed
        if (distance >= 5.0 && distance < 5.035) {
            sendNotification(userId, "🏅 Amazing! You completed 5KM!");
        }

        // Milestone 3 — Personal best pace
        if (pace < personalBestPace) {
            personalBestPace = pace;
            sendNotification(userId, "⚡ New Personal Best Pace: "
                    + String.format("%.2f", pace) + " min/km!");
        }
    }

    private void sendNotification(String userId, String message) {
        // In production this would send email/SMS/push notification
        // For now we print to console
        System.out.println("🔔 NOTIFICATION for " + userId + ": " + message);
    }
}