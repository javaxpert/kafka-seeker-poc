package com.skyreelmedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skyreelmedia.kafka.KafkaRecordReprocessor;
import com.skyreelmedia.kafka.TestEvent;
import com.skyreelmedia.kafka.TestEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class JavaKafkaTestApplication {

    public static void main(String[] args) throws InterruptedException {
        var appContext = SpringApplication.run(JavaKafkaTestApplication.class, args);
        var producer = appContext.getBean(TestEventProducer.class);
        try {
            for (int i = 0; i < 100; i++) {
                producer.publish(new TestEvent(Long.valueOf(i+1), "Test event %05d".formatted(i+1)));
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info("Finished publishing events");
        var reprocessor = appContext.getBean(KafkaRecordReprocessor.class);
        reprocessor.reprocessFromOffset(50, 10);
    }

}
