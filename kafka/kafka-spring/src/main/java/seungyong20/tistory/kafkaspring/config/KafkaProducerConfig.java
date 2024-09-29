package seungyong20.tistory.kafkaspring.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import seungyong20.tistory.kafkaspring.dto.RequestKafkaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 메시지를 보내기 위한 ProducerFactory를 생성합니다.
     * Key는 String, Value는 RequestKafkaDto로 설정합니다.
     */
    @Bean
    public ProducerFactory<String, RequestKafkaDto> producerFactory() {
        log.info("Creating producer factory");

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * KafkaTemplate을 생성합니다.
     * Key는 String, Value는 RequestKafkaDto로 설정합니다.
     */
    @Bean
    public KafkaTemplate<String, RequestKafkaDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
