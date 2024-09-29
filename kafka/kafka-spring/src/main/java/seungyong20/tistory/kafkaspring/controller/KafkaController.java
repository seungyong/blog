package seungyong20.tistory.kafkaspring.controller;

import org.springframework.web.bind.annotation.ResponseBody;
import seungyong20.tistory.kafkaspring.dto.RequestKafkaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seungyong20.tistory.kafkaspring.util.KafkaProducer;

@RequiredArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<?> sendMessage() {

        for (int i = 0; i < 10000; i++) {
            kafkaProducer.sendMessage("python_topic", "test_key", new RequestKafkaDto((long) i, "seungyong " + i));
        }

        return ResponseEntity.noContent().build();
    }
}
