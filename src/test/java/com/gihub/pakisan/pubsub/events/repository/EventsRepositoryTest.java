package com.gihub.pakisan.pubsub.events.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gihub.pakisan.pubsub.exchangerates.ExchangeRate;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Transactional
@SpringBootTest
public class EventsRepositoryTest {

    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public static Stream<Arguments> save() {
        return Stream.of(
                Arguments.of(
                        new Event(
                                UUID.randomUUID(),
                                Date.from(Instant.now()),
                                EventType.STOCKS,
                                new StockRate("NIFTY 50", Date.from(Instant.now()), 17319.30, 45.00)
                        )
                ),
                Arguments.of(
                        new Event(
                                UUID.randomUUID(),
                                Date.from(Instant.now()),
                                EventType.EXCHANGE_RATES,
                                new ExchangeRate("Artsakhbank", Date.from(Instant.now()), "USD", 406L, 416L)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("save")
    public void save(@Nonnull Event event) {
        Assertions.assertEquals(event, eventsRepository.save(event));
    }

    public static Stream<Arguments> findEventsByType() {
        return Stream.of(
                Arguments.of(
                        "/exchangeRateEvents.json",
                        EventType.EXCHANGE_RATES
                ),
                Arguments.of(
                        "/stockRateEvents.json",
                        EventType.STOCKS
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findEventsByType")
    public void findEventsByType(@Nonnull String eventsLocation, @Nonnull EventType eventType) throws IOException {
        var expectedEvents = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventsLocation),
                new TypeReference<List<Event>>() {}
        );
        eventsRepository.saveAll(expectedEvents);

        Assertions.assertEquals(expectedEvents, eventsRepository.findAllByType(eventType));
    }

}
