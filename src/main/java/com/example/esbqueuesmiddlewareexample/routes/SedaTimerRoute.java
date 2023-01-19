package com.example.esbqueuesmiddlewareexample.routes;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

//@Component
public class SedaTimerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:ping?period=1000")
            .process(exchange -> {
                Message message = new DefaultMessage(exchange);
                message.setBody(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
                exchange.setMessage(message);
            })
            .to("seda:weighLifter?multipleConsumers=true");

        from("seda:weighLifter?multipleConsumers=true")
            .to("direct:complex-seda-process");

        from("direct:complex-seda-process")
            .log("Current time from SEDA route: ${body}")
            .process(exchange -> SECONDS.sleep(5))
            .end();
    }
}
