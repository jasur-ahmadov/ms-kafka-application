package mskafkaapplication.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.producer.topic}")
    public String topicName;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(topicName)
                .partitions(3)
                .compact() // Kafka will retain the latest message for each key across these partitions.
                .replicas(1)
                .build();
    }
}