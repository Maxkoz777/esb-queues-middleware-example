package com.example.esbqueuesmiddlewareexample.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestToRestRoute extends RouteBuilder {

    @Override
    public void configure() {

        restConfiguration().host("localhost").port(8080);

        from("timer:hello?period={{timer.period}}")
            .setHeader("id", simple("${random(6,9)}"))
            .to("rest:get:api/v1/message/{id}")
            .log("${body}");


    }
}
