package com.example.esbqueuesmiddlewareexample.routes.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleLoggingProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        log.info("Inside Simple Logging Processor");
        log.info("Message body from exchange: {}", exchange.getMessage().getBody());
    }

}
