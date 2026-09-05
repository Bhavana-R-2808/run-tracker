package com.runtracker.producer;

import com.runtracker.model.RunEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GpsProducerService {

    private static final String TOPIC = "run-events";

    @Autowired
    private KafkaTemplate<String, RunEvent> kafkaTemplate;

    public void sendEvent(RunEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
        System.out.println("Event sent to Kafka: " + event);
    }
}
