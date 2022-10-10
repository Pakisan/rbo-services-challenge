package com.gihub.pakisan.pubsub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gihub.pakisan.pubsub.events.repository.Event;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import com.gihub.pakisan.pubsub.events.repository.EventType;
import com.gihub.pakisan.pubsub.exchangerates.ExchangeRate;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class ObjectMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    public static Stream<Arguments> readEvents() {
        return Stream.of(
                Arguments.of(
                        "/exchangeRateEvents.json",
                        ExchangeRate.class
                ),
                Arguments.of(
                        "/stockRateEvents.json",
                        StockRate.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource("readEvents")
    public void readEvents(@Nonnull String eventsLocation, Class<? extends EventPayload> expectedPayloadType) throws IOException {
        var events = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventsLocation),
                new TypeReference<List<Event>>() {}
        );

        for (Event event: events) {
            Assertions.assertEquals(expectedPayloadType, event.getPayload().getClass());
        }
    }

}
