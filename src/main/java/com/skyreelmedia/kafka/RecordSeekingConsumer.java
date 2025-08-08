package com.skyreelmedia.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class RecordSeekingConsumer implements ConsumerSeekAware {

    private final ConsumerFactory<String, String> consumerFactory;
    private final ObjectMapper objectMapper;
    private final String topic;
//  Important! Not a thread safe consumer
    private KafkaConsumer<String, String> consumer;

    public void initialize(String groupId) {
        var props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer(
                groupId,
                null,
                null,
                props
        );
    }
    // Add these fields to track processing
    private long recordsToProcess;
    private long recordsProcessed;
    private boolean isReprocessing;

    // Add method to process records
    public void reprocessFromOffset(long startingOffset, long numberOfRecords, Map<TopicPartition, Long> partitionOffsets) {
        try {
            recordsToProcess = numberOfRecords;
            recordsProcessed = 0;
            isReprocessing = true;

            // Seek to the desired offset
            reprocessFromOffset(startingOffset, partitionOffsets);

            // Process records until we reach the desired count
            while (isReprocessing && recordsProcessed < recordsToProcess) {
                var records = consumer.poll(Duration.ofMillis(100));

                for (var record : records) {
                    processRecord(record);
                    recordsProcessed++;

                    if (recordsProcessed >= recordsToProcess) {
                        log.info("Finished processing {} records, moving back to latest", recordsProcessed);
                        moveToLatest(partitionOffsets);
                        isReprocessing = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error during record processing", e);
            moveToLatest(partitionOffsets); // Ensure we return to latest on error
        } finally {
            isReprocessing = false;
            recordsProcessed = 0;
            recordsToProcess = 0;
        }
    }


    public void reprocessLastNRecords(int n) {
        consumer.assignment().forEach(partition -> {
            long currentOffset = consumer.position(partition);
            long newOffset = Math.max(0, currentOffset - n);
            consumer.seek(partition, newOffset);
        });
    }

    public void reprocessFromOffset(long offset, Map<TopicPartition, Long> partitionOffsets) {
        consumer.assign(partitionOffsets.keySet());
        partitionOffsets.keySet().forEach(partition -> {
            long latestOffset = partitionOffsets.get(partition);
            log.info("Latest offset for partition {}: {}", partition, latestOffset);
            long newOffset = Math.max(0, latestOffset - offset);
            log.info("Seeking to offset {} for partition {}", newOffset, partition);
            consumer.seek(partition, newOffset);
        });
    }

    public void moveToLatest(Map<TopicPartition, Long> partitionOffsets) {
        consumer.assignment().forEach(partition ->
                consumer.seekToEnd(Set.of(partition))
        );
        partitionOffsets.keySet().forEach(partition -> {
            long latestOffset = partitionOffsets.get(partition);
            log.info("Returned to the latest offset of {} for partition {}", latestOffset, partition);
        });
    }

    public void reprocessFromBeginning() {
        consumer.assignment().forEach(partition ->
                consumer.seekToBeginning(Set.of(partition))
        );
    }

    public void close() {
        if (consumer != null) {
            consumer.close();
        }
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        try {
            var event = objectMapper.readValue(record.value(), TestEvent.class);
            log.info("Processed record at offset {}: {}", record.offset(), event);
        } catch (Exception e) {
            log.error("Error processing record at offset {}", record.offset(), e);
        }
    }

}
