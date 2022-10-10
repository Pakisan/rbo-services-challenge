package com.gihub.pakisan.pubsub.events.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gihub.pakisan.pubsub.events.dto.InEventDto;
import com.gihub.pakisan.pubsub.events.dto.OutEventDto;
import com.gihub.pakisan.pubsub.events.dto.exchangerates.InExchangeRateEventDto;
import com.gihub.pakisan.pubsub.events.dto.exchangerates.OutExchangeRateEventDto;
import com.gihub.pakisan.pubsub.events.dto.stocks.InStockRateEventDto;
import com.gihub.pakisan.pubsub.events.dto.stocks.OutStockRateEventDto;
import com.gihub.pakisan.pubsub.events.repository.Event;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class EventsConverterTest {

    @Autowired
    private EventsConverter eventsConverter;
    @Autowired
    private ObjectMapper objectMapper;

    public static Stream<Arguments> convertInEventDto() {
        return Stream.of(
                Arguments.of(
                        "/exchangeRateEvent.json",
                        "/inExchangeRateEventDto.json",
                        InExchangeRateEventDto.class
                ),
                Arguments.of(
                        "/stockRateEvent.json",
                        "/inStockRateEventDto.json",
                        InStockRateEventDto.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource("convertInEventDto")
    @DisplayName("convert InEventDto")
    public void convertInEventDto(@Nonnull String eventLocation,
                                  @Nonnull String inEventDtoLocation,
                                  @Nonnull Class<? extends InEventDto> inEventDtoClass
    ) throws IOException {
        var expectedEvent = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventLocation),
                Event.class
        );
        var inEventDto = objectMapper.readValue(
                this.getClass().getResourceAsStream(inEventDtoLocation),
                inEventDtoClass
        );

        try (var uuidMock = Mockito.mockStatic(UUID.class); var dateMock = Mockito.mockStatic(Date.class)) {
            uuidMock.when(UUID::randomUUID).thenReturn(expectedEvent.getId());
            dateMock.when((MockedStatic.Verification) Date.from(any())).thenReturn(expectedEvent.getCreatedAt());

            Assertions.assertEquals(expectedEvent, eventsConverter.convert(inEventDto));
        }
    }

    public static Stream<Arguments> convertOutEventDto() {
        return Stream.of(
                Arguments.of(
                        "/exchangeRateEvent.json",
                        "/outExchangeRateEventDto.json",
                        OutExchangeRateEventDto.class
                ),
                Arguments.of(
                        "/stockRateEvent.json",
                        "/outStockRateEventDto.json",
                        OutStockRateEventDto.class
                )
        );
    }

    @ParameterizedTest
    @MethodSource("convertOutEventDto")
    @DisplayName("convert OutEventDto")
    public void convertOutEventDto(@Nonnull String eventLocation,
                                   @Nonnull String outEventDtoLocation,
                                   @Nonnull Class<? extends OutEventDto<? extends EventPayload>> outEventDtoClass
    ) throws IOException {
        var event = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventLocation),
                Event.class
        );
        var outEventDto = objectMapper.readValue(
                this.getClass().getResourceAsStream(outEventDtoLocation),
                outEventDtoClass
        );

        Assertions.assertEquals(outEventDto, eventsConverter.convert(event));
    }

    public static Stream<Arguments> convertOutEventDtos() {
        return Stream.of(
                Arguments.of(
                        "/exchangeRateEvents.json",
                        "/outExchangeRateEventsDto.json",
                        new TypeReference<List<OutExchangeRateEventDto>>() {}
                ),
                Arguments.of(
                        "/stockRateEvents.json",
                        "/outStockRateEventsDto.json",
                        new TypeReference<List<OutStockRateEventDto>>() {}

                )
        );
    }

    @ParameterizedTest
    @MethodSource("convertOutEventDtos")
    @DisplayName("convert Events")
    public void convertOutEventDtos(@Nonnull String eventsLocation,
                                    @Nonnull String outEventsDtoLocation,
                                    @Nonnull TypeReference<List<? extends OutEventDto<? extends EventPayload>>> outEventDtoClass
    ) throws IOException {
        var event = objectMapper.readValue(
                this.getClass().getResourceAsStream(eventsLocation),
                new TypeReference<List<Event>>() {}
        );
        var outEventDto = objectMapper.readValue(
                this.getClass().getResourceAsStream(outEventsDtoLocation),
                outEventDtoClass
        );

        Assertions.assertEquals(outEventDto, eventsConverter.convert(event));
    }

}
