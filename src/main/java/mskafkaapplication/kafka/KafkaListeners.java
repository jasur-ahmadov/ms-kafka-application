package mskafkaapplication.kafka;

import lombok.extern.slf4j.Slf4j;
import mskafkaapplication.dto.StudentResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListeners {

    @KafkaListener(topics = "student-topic", groupId = "groupId")
    void listener(StudentResponse response) {
        log.info("Listener received data : {}, ", response);
    }
}