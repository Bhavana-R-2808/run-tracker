package com.runtracker.consumer;

import com.runtracker.model.RunEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LeaderboardConsumer {

    private static final String LEADERBOARD_KEY = "leaderboard:route1";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "run-events", groupId = "leaderboard-group")
    public void consumeRunEvent(RunEvent event) {

        System.out.println("Consumer 2 received event for user: " + event.getUserId());

        // Update leaderboard in Redis
        // Lower pace = faster runner = better rank
        // We store pace as score — Redis sorts automatically
        redisTemplate.opsForZSet().add(
                LEADERBOARD_KEY,
                event.getUserId(),
                event.getPace()
        );

        System.out.println("Leaderboard updated for user: "
                + event.getUserId()
                + " with pace: " + event.getPace());
    }
}