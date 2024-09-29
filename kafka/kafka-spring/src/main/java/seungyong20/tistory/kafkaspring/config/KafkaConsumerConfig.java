package seungyong20.tistory.kafkaspring.config;

import com.fasterxml.jackson.core.JsonParseException;
import seungyong20.tistory.kafkaspring.dto.ResponseKafkaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;
    private final Long interval = 1000L;
    private final Long maxAttempts = 3L;

    @Bean
    public ConsumerFactory<String, ResponseKafkaDto> consumerFactory() {
        log.info("Creating consumer factory");

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 각 Consumer 인스턴스를 식별하기 위한 고유한 ID를 생성합니다.
        configProps.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, UUID.randomUUID().toString());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // JSON 데이터를 ResponseKafkaDto로 역직렬화하기 위한 JsonDeserializer를 생성합니다.
        JsonDeserializer<ResponseKafkaDto> deserializer = new JsonDeserializer<>(ResponseKafkaDto.class);
        // ErrorHandlingDeserializer를 사용하여 예외가 발생했을 때 처리를 수행합니다.
        ErrorHandlingDeserializer<ResponseKafkaDto> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        // Kafka ConsumerFactory를 생성합니다. Key는 String, Value는 ResponseKafkaDto로 설정하고, 에러 발생 시 처리를 위해 ErrorHandlingDeserializer를 사용합니다.
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResponseKafkaDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ResponseKafkaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(5);
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) -> {
            log.error("Error occurred while processing: {} and value : {}", e, consumerRecord.value());
        }, fixedBackOff);

        errorHandler.addRetryableExceptions(SocketTimeoutException.class);
        errorHandler.addNotRetryableExceptions(NullPointerException.class);
        errorHandler.addNotRetryableExceptions(JsonParseException.class);
        errorHandler.addNotRetryableExceptions(SerializationException.class);

        return errorHandler;
    }
}
