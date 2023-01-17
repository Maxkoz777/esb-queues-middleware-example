package com.example.esbqueuesmiddlewareexample.controller;

import com.example.esbqueuesmiddlewareexample.config.props.KafkaConfigurationProperties;
import com.example.esbqueuesmiddlewareexample.model.messages.SimpleMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/messages")
public class MessageController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfigurationProperties kafkaProperties;

    @GetMapping
    public ResponseEntity<Void> sendSimpleMessage() {
        val id = UUID.randomUUID().toString();
        log.info("Manually sending message with id {}", id);
        kafkaTemplate.send(kafkaProperties.entryQueueName(), id, new SimpleMessage("A simple message", id));
//        kafkaTemplate.send(kafkaProperties.entryQueueName(), id, "A simple message");
        return ResponseEntity.ok().build();
    }

}
