package com.example.esbqueuesmiddlewareexample.routes;

import com.example.esbqueuesmiddlewareexample.config.props.KafkaConfigurationProperties;
import com.example.esbqueuesmiddlewareexample.routes.processors.BodyChangeProcessor;
import com.example.esbqueuesmiddlewareexample.routes.processors.SimpleLoggingProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaToKafkaRoute extends RouteBuilder {

    private final KafkaConfigurationProperties kafkaConfigurationProperties;
    private final SimpleLoggingProcessor simpleLoggingProcessor;

    private final BodyChangeProcessor bodyChangeProcessor;
    private static final String AUDIT_TRANSACTION = "direct:audit-transaction";

    @Override
    public void configure() {
        from(kafkaEntryQueue())
            .wireTap(AUDIT_TRANSACTION)
            .log("Redirecting message from entry kafka queue to the output queue")
            .log("Initial message was: ${body}")
            .log("Setting message body into: Changed message by Camel Route")
            .bean(simpleLoggingProcessor)
            .bean(bodyChangeProcessor)
            .log("Updated message body after processor: ${body}")
            .to(kafkaFinalQueue());

        from(AUDIT_TRANSACTION)
            .log("Example of wire tap EIP")
            .log("Sending message to audit queue")
            .to(kafkaAuditQueue());

        from(kafkaAuditQueue())
            .log("Reading from audit queue")
            .log("Message from audit queue event: ${body}");

    }

    private String kafkaEntryQueue() {
        return getKafkaRouteByTopicName(kafkaConfigurationProperties.entryQueueName());
    }

    private String kafkaFinalQueue() {
        return getKafkaRouteByTopicName(kafkaConfigurationProperties.finalQueueName());
    }

    private String kafkaAuditQueue() {
        return getKafkaRouteByTopicName(kafkaConfigurationProperties.auditQueueName());
    }

    private String getKafkaRouteByTopicName(String topicName) {
        return "kafka:" + topicName + "?brokers=localhost:9092";
    }

}
