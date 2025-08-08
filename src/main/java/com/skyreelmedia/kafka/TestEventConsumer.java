package com.skyreelmedia.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

import static com.skyreelmedia.kafka.KafkaConfiguration.TOPIC;


@Slf4j
@RequiredArgsConstructor
public class TestEventConsumer {
    private final ObjectMapper objectMapper;

    @Getter
    private Map<TopicPartition, Long> partitionOffsets;

    @KafkaListener(topics = TOPIC, id = "test-event-consumer")
    public void consume(String message, Consumer<String, String> consumer) throws JsonProcessingException {
        var event = objectMapper.readValue(message, TestEvent.class);
        partitionOffsets = consumer.endOffsets(consumer.assignment());
        log.info("Received event: {}. Updated offsets: {}", event, partitionOffsets);
    }
}
