package seungyong20.tistory.kafkaspring.util;

import seungyong20.tistory.kafkaspring.dto.RequestKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, RequestKafkaDto> kafkaTemplate;

    public void sendMessage(String topic, String key, RequestKafkaDto requestKafkaDto) {
        log.info("Producing {} message: {}", key, requestKafkaDto);
        kafkaTemplate.send(topic, key, requestKafkaDto);
    }
}
