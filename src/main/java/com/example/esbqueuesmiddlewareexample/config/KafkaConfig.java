package com.example.esbqueuesmiddlewareexample.config;

import com.example.esbqueuesmiddlewareexample.config.props.KafkaConfigurationProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaConfigurationProperties.class)
@ComponentScan(value = "com.example.esbqueuesmiddlewareexample.config.props")
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaConfigurationProperties configurationProperties;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
            new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                  StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                  JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic inputKafkaTopic() {
        return new NewTopic(configurationProperties.entryQueueName(), 3, (short) 1);
    }

    @Bean
    public NewTopic outputKafkaTopic() {
        return new NewTopic(configurationProperties.finalQueueName(), 3, (short) 1);
    }

    @Bean
    public NewTopic auditKafkaTopic() {
        return new NewTopic(configurationProperties.auditQueueName(), 3, (short) 1);
    }


    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
            kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    // String Consumer Configuration

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new StringDeserializer()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());

        return factory;
    }

    // Byte Array Consumer Configuration

    @Bean
    public ConsumerFactory<String, byte[]> byteArrayConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
            kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new ByteArrayDeserializer()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerByteArrayContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(byteArrayConsumerFactory());
        return factory;
    }

}
