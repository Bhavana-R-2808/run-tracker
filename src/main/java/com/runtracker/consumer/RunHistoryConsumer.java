package com.runtracker.consumer;

import com.runtracker.entity.RunRecord;
import com.runtracker.model.RunEvent;
import com.runtracker.repository.RunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RunHistoryConsumer {

    @Autowired
    private RunRepository runRepository;

    @KafkaListener(topics = "run-events", groupId = "history-group")
    public void consumeRunEvent(RunEvent event) {

        System.out.println("Consumer 1 received event for user: " + event.getUserId());

        // Step 1 — Create RunRecord from RunEvent
        RunRecord record = new RunRecord(
                event.getUserId(),
                event.getRunId(),
                event.getLatitude(),
                event.getLongitude(),
                event.getPace(),
                event.getDistanceKm(),
                event.getTimestamp()
        );

        // Step 2 — Save to MySQL
        runRepository.save(record);

        System.out.println("Saved to MySQL: " + record);
    }
}