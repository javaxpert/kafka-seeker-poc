package com.skyreelmedia.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import static com.skyreelmedia.kafka.KafkaConfiguration.TOPIC;

@AllArgsConstructor
public class TestEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(TestEvent testEvent) throws JsonProcessingException {
        var messagePayload = objectMapper.writeValueAsString(testEvent);
        kafkaTemplate.send(TOPIC, 1, "key", messagePayload);
    }
}
