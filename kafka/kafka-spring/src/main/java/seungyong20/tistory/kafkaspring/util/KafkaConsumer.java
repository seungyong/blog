package seungyong20.tistory.kafkaspring.util;

import seungyong20.tistory.kafkaspring.dto.ResponseKafkaDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@Component
public class KafkaConsumer {
    @KafkaListener(topics = "spring_topic")
    public void listen(ConsumerRecord<String, ResponseKafkaDto> record, Acknowledgment acknowledgment) {
        try {
            String key = record.key();
            ResponseKafkaDto payload = record.value();

            log.info("Received message: key={}, id={}, message={}", key, payload.getId(), payload.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while consuming message: {}", e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
