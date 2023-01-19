package com.example.esbqueuesmiddlewareexample.routes.processors;

import com.example.esbqueuesmiddlewareexample.model.messages.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BodyChangeProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        val changedMessage = "Changed message by Camel Route";
        log.info("Changing Message body");
        Message message = new DefaultMessage(exchange);
        val changedMessageBody = new SimpleMessage(changedMessage, exchange.getExchangeId());
        message.setBody(changedMessageBody, SimpleMessage.class);
        log.info("Message is set with new text: {}", changedMessage);
        exchange.setMessage(message);
    }
}
