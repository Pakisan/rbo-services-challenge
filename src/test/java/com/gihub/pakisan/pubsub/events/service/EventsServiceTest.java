package com.gihub.pakisan.pubsub.events.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gihub.pakisan.pubsub.events.dto.InEventDto;
import com.gihub.pakisan.pubsub.events.dto.exchangerates.InExchangeRateEventDto;
import com.gihub.pakisan.pubsub.events.dto.stocks.InStockRateEventDto;
import com.gihub.pakisan.pubsub.events.repository.Event;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import com.gihub.pakisan.pubsub.events.repository.EventType;
import com.gihub.pakisan.pubsub.events.repository.EventsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@Transactional
@SpringBootTest
public class EventsServiceTest {

    @Autowired
    private EventsService eventsService;
    @Autowired
    private EventsConverter eventsConverter;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("return null when Event type is null")
    public void returnNullWhenEventTypeIsNull() {
        Assertions.assertNull(eventsService.find(null));
    }

    public static Stream<Arguments> returnEmptyCollectionWhenEventsDontExist() {
        return Stream.of(
                Arguments.of(EventType.STOCKS),
                Arguments.of(EventType.EXCHANGE_RATES)
        );
    }

    @ParameterizedTest
    @MethodSource("returnEmptyCollectionWhenEventsDontExist")
    @DisplayName("return empty collection when Events don't exists")
    public void returnEmptyCollectionWhenEventsDontExist(@Nonnull EventType eventType) {
        var events = eventsService.find(eventType);
        Assertions.assertTrue(events.isEmpty());
    }

    public static Stream<Arguments> returnEvents() {
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
    @MethodSource("returnEvents")
    @DisplayName("return Events")
    public void returnEvents(@Nonnull String eventsLocation, @Nonnull EventType eventType) throws IOException {
        var expectedEvents = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventsLocation),
                new TypeReference<List<Event>>() {}
        );
        eventsRepository.saveAll(expectedEvents);

        Assertions.assertEquals(expectedEvents, eventsService.find(eventType));
    }

    public static Stream<Arguments> saveAndGetNullWhenOneOfParamsIsNull() {
        return Stream.of(
                Arguments.of(
                        null,
                        "/inExchangeRateEventDto.json",
                        InExchangeRateEventDto.class
                ),
                Arguments.of(
                        null,
                        "/inStockRateEventDto.json",
                        InStockRateEventDto.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource("saveAndGetNullWhenOneOfParamsIsNull")
    @DisplayName("save and get null when one of params is null")
    public void saveAndGetNullWhenOneOfParamsIsNull(@CheckForNull EventType eventType,
                                                    @Nonnull String inEventDtoLocation,
                                                    @Nonnull Class<? extends InEventDto> inEventDtoClass) throws IOException {
        var eventDto = objectMapper.readValue(
                this.getClass().getResourceAsStream(inEventDtoLocation),
                inEventDtoClass
        );

        Assertions.assertNull(eventsService.save(eventType, eventsConverter.convert(eventDto)));
    }

    public static Stream<Arguments> saveAndReturnEvent() {
        return Stream.of(
                Arguments.of(
                        "/exchangeRateEvent.json",
                        EventType.EXCHANGE_RATES,
                        "/inExchangeRateEventDto.json",
                        InExchangeRateEventDto.class
                ),
                Arguments.of(
                        "/stockRateEvent.json",
                        EventType.STOCKS,
                        "/inStockRateEventDto.json",
                        InStockRateEventDto.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource("saveAndReturnEvent")
    @DisplayName("save and return Event")
    public void saveAndReturnEvent(@Nonnull String eventLocation,
                                   @Nonnull EventType eventType,
                                   @Nonnull String inEventDtoLocation,
                                   @Nonnull Class<? extends InEventDto> inEventDtoClass) throws IOException {
        var expectedEvent = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventLocation),
                Event.class
        );
        var eventDto = objectMapper.readValue(
                this.getClass().getResourceAsStream(inEventDtoLocation),
                inEventDtoClass
        );

        try (var uuidMock = Mockito.mockStatic(UUID.class); var dateMock = Mockito.mockStatic(Date.class)) {
            uuidMock.when(UUID::randomUUID).thenReturn(expectedEvent.getId());
            dateMock.when((MockedStatic.Verification) Date.from(any())).thenReturn(expectedEvent.getCreatedAt());

            Assertions.assertEquals(expectedEvent, eventsService.save(eventType, eventsConverter.convert(eventDto)));
        }
    }

}