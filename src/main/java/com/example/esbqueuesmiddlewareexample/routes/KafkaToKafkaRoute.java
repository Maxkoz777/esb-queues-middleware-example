package com.example.esbqueuesmiddlewareexample.routes;

import com.example.esbqueuesmiddlewareexample.config.props.KafkaConfigurationProperties;
import com.example.esbqueuesmiddlewareexample.routes.processors.SimpleLoggingProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaToKafkaRoute extends RouteBuilder {

    private final KafkaConfigurationProperties kafkaConfigurationProperties;
    private final SimpleLoggingProcessor simpleLoggingProcessor;

    @Override
    public void configure() throws Exception {
        from(kafkaEntryQueue())
            .log("Redirecting message from entry kafka queue to the output queue")
            .log("Initial message was: ${body}")
            .log("Setting message body into: Changed message by Camel Route")
            .bean(simpleLoggingProcessor)
//            .setBody(constant("Changed message by Camel Route"))
//            .setHeader(KafkaConstants.KEY, constant("Camel"))
            .to(kafkaFinalQueue());
    }

    private String kafkaEntryQueue() {
        return getKafkaRouteByTopicName(kafkaConfigurationProperties.entryQueueName());
    }

    private String kafkaFinalQueue() {
        return getKafkaRouteByTopicName(kafkaConfigurationProperties.finalQueueName());
    }

    private String getKafkaRouteByTopicName(String topicName) {
        return "kafka:" + topicName + "?brokers=localhost:9092";
    }

}
