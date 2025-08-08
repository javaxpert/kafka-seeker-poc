package com.skyreelmedia.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.skyreelmedia.kafka.KafkaConfiguration.TOPIC;


@AllArgsConstructor
@Slf4j
public class TestEventReconcilingConsumer {
    private final ObjectMapper objectMapper;

//    @KafkaListener(topics = TOPIC, id = "test-event-reconciling-consumer")
    public void consume(String message, Consumer<String, String> consumer) throws JsonProcessingException {
        // First, get all partitions for the topic
        var partitions = consumer.partitionsFor(TOPIC);
        // Create TopicPartition objects and collect them
        var topicPartitions = new ArrayList<TopicPartition>();
        for (var partitionInfo : partitions) {
            var topicPartition = new TopicPartition(TOPIC, partitionInfo.partition());
            topicPartitions.add(topicPartition);
        }

        // For each partition, seek to end offset - 20 (or beginning if less than 20 messages)
        seekToOffset(-20, consumer, topicPartitions);

        // Process the current message
        var event = objectMapper.readValue(message, TestEvent.class);
        log.info("Reconciling event: {}", event);
    }


    private void seekToOffset(long offset, Consumer<String, String> consumer, List<TopicPartition> topicPartitions) {
        Map<TopicPartition, Long> endOffsets = new HashMap<>();
        for (var topicPartition : topicPartitions) {
            long endOffset = endOffsets.get(topicPartition);
            long targetOffset = Math.max(0, endOffset - offset);
            consumer.seek(topicPartition, targetOffset);
            log.info("Partition {}: Seeking to offset {} (end offset: {})",
                    topicPartition.partition(), targetOffset, endOffset);
        }
    }
}
