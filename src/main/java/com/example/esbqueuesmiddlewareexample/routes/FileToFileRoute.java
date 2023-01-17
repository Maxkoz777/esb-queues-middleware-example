package com.example.esbqueuesmiddlewareexample.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileToFileRoute extends RouteBuilder {

    @Override
    public void configure() {
        from(folderPathByName("input"))
            .to(folderPathByName("output"));
    }

    private String folderPathByName(String folderName) {
        return "file:files/" + folderName;
    }

}
