package com.example.esbqueuesmiddlewareexample.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "queue.kafka")
public record KafkaConfigurationProperties(
    String entryQueueName,
    String finalQueueName
) {}
