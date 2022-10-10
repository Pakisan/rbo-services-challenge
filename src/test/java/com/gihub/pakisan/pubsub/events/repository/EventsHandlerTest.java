package com.gihub.pakisan.pubsub.events.repository;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@SpringBootTest
public class EventsHandlerTest {

    private static ListAppender<ILoggingEvent> listAppender;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EventsRepository eventsRepository;

    @BeforeEach
    public void initContext() {
        Logger logger = (Logger) LoggerFactory.getLogger(EventsHandler.class);
        listAppender = new ListAppender<>();
        listAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(listAppender);
        listAppender.start();
        eventsRepository.deleteAll();
    }

    @AfterEach
    public void destroyContext() {
        listAppender.stop();
        eventsRepository.deleteAll();
    }

    @Test
    @DisplayName("new Event MUST appear in logs")
    public void newEventMustAppearInLogs() throws JsonProcessingException {
        var event = eventsRepository.save(
                new Event(
                        UUID.randomUUID(),
                        Date.from(Instant.now()),
                        EventType.STOCKS,
                        new StockRate("NIFTY 50", Date.from(Instant.now()), 17319.30, 45.00)
                )
        );

        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Yerevan"));
        Assertions.assertEquals(
                event,
                objectMapper.readValue(
                        listAppender.list.get(0).getFormattedMessage()
                                .substring("event:".length()).trim(),
                        Event.class
                )
        );
    }

}
