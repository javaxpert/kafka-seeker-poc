package com.skyreelmedia.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class KafkaRecordReprocessor {
    private final TestEventConsumer testEventConsumer;
    private final RecordSeekingConsumer recordSeekingConsumer;

    public void reprocessRecords(int numberOfRecords) {
        try {
            // Initialize with a unique group ID
            recordSeekingConsumer.initialize("reprocess-" + System.currentTimeMillis());
            // Perform reprocessing
            recordSeekingConsumer.reprocessLastNRecords(numberOfRecords);
        } finally {
            // Clean up
            recordSeekingConsumer.close();
        }
    }
    
    public void reprocessFromOffset(long offset, long numberOfRecs) {
        try {
            // Initialize with a unique group ID
            recordSeekingConsumer.initialize("reprocess-offset-" + System.currentTimeMillis());
            // Wait until other consumers have been assigned partitions
            while(testEventConsumer.getPartitionOffsets() == null || isEmpty(testEventConsumer.getPartitionOffsets().keySet()))  {
                Thread.sleep(1000);
            }
            // Perform reprocessing from a specific offset
            recordSeekingConsumer.reprocessFromOffset(offset, numberOfRecs, testEventConsumer.getPartitionOffsets());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // Clean up
            recordSeekingConsumer.close();
        }
    }
}
