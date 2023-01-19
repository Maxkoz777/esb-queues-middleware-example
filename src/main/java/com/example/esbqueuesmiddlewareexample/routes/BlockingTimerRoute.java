package com.example.esbqueuesmiddlewareexample.routes;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

//@Component
public class BlockingTimerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:ping?period=1000")
            .process(exchange -> {
                Message message = new DefaultMessage(exchange);
                message.setBody(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
                exchange.setMessage(message);
            })
            .to("direct:complex-process");

        from("direct:complex-process")
            .log("Current time from blocking route: ${body}")
            .process(exchange -> SECONDS.sleep(5))
            .end();
    }
}
