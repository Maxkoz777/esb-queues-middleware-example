package com.example.esbqueuesmiddlewareexample.model.consumer;


import com.example.esbqueuesmiddlewareexample.model.messages.SimpleMessage;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class KafkaEntryConsumer {

    @KafkaListener(topics = "kafka-entry-queue", clientIdPrefix = "json",
        containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, SimpleMessage> cr,
                               @Payload SimpleMessage payload) {
        log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr);
    }

    @KafkaListener(topics = "kafka-entry-queue", clientIdPrefix = "string",
        containerFactory = "kafkaListenerStringContainerFactory")
    public void listenAsString(ConsumerRecord<String, String> cr,
                               @Payload String payload) {
        log.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr);
    }

    @KafkaListener(topics = "kafka-entry-queue", clientIdPrefix = "bytearray",
        containerFactory = "kafkaListenerByteArrayContainerFactory")
    public void listenAsByteArray(ConsumerRecord<String, byte[]> cr,
                                  @Payload byte[] payload) {
        log.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr);
    }

    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
            .filter(header -> header.key().equals("__TypeId__"))
            .findFirst().map(header -> new String(header.value())).orElse("N/A");
    }



}
