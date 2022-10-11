package com.gihub.pakisan.pubsub.events.repository;

import com.gihub.pakisan.pubsub.events.api.sse.EventsEmitterHolder;
import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class EventsHandler {

    private final PGConnection connection;

    public EventsHandler(@Nonnull String url, @Nonnull EventsEmitterHolder eventsEmitterHolder) throws SQLException {
        connection = DriverManager.getConnection(url).unwrap(PGConnection.class);
        connection.addNotificationListener(new PGNotificationListener() {
            @Override
            public void notification(int processId, String channelName, String payload) {
                log.info("event: {}", payload);
                eventsEmitterHolder.broadcast(EventType.STOCKS, payload);
            }
        });
    }

    public void init() throws Throwable {
        log.info("init()");
        var statement = connection.createStatement();
        statement.execute("LISTEN events_events");
        statement.close();
    }

    public void destroy() throws Throwable {
        var statement = connection.createStatement();
        statement.execute("UNLISTEN events_events");
        statement.close();
    }

}