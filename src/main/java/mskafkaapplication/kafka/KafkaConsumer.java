package mskafkaapplication.kafka;

import lombok.extern.slf4j.Slf4j;
import mskafkaapplication.dto.StudentResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "${app.kafka.producer.topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    void consumerListener(StudentResponse response) {
        log.info("Consumer received data: {}", response);
    }
}