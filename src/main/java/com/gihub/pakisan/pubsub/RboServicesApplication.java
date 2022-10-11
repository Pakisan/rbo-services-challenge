package com.gihub.pakisan.pubsub;

import com.gihub.pakisan.pubsub.events.api.sse.EventsEmitterHolder;
import com.gihub.pakisan.pubsub.events.repository.EventsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@SpringBootApplication
public class RboServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RboServicesApplication.class, args);
    }

    @Bean(name = "eventsHandler", initMethod = "init", destroyMethod = "destroy")
    @Autowired
    public EventsHandler eventsHandler(@Value("${spring.datasource.url}") String url,
                                       EventsEmitterHolder eventsEmitterHolder) throws SQLException {
        return new EventsHandler(url, eventsEmitterHolder);
    }

}
