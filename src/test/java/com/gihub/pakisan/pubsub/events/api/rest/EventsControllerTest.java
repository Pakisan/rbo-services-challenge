package com.gihub.pakisan.pubsub.events.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gihub.pakisan.pubsub.TestHelpers;
import com.gihub.pakisan.pubsub.events.dto.OutEventDto;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Nonnull;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private TestHelpers testHelpers;

    @Test
    @DisplayName("get 404 when Event type doesn't exists")
    public void get404WhenEvenTypeDoesntExists() throws Exception {
        this.mockMvc.perform(
                        get(EventsController.ENDPOINT + "/RATES")
                ).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    public static Stream<Arguments> get200WhenEventsDoesntExists() {
        return Stream.of(
                Arguments.of(
                        EventType.STOCKS.toString()
                ),
                Arguments.of(
                        EventType.EXCHANGE_RATES.toString()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("get200WhenEventsDoesntExists")
    @DisplayName("get 200 when Events doesn't exists")
    public void get200WhenEventsDoesntExists(@Nonnull String eventType) throws Exception {
        this.mockMvc.perform(
                        get(EventsController.ENDPOINT + "/" + eventType)
                ).andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    public static Stream<Arguments> get200WhenEventsExist() {
        return Stream.of(
                Arguments.of(
                        EventType.STOCKS.toString(),
                        "/stockRateEvents.json",
                        "/outStockRateEventsDto.json"
                ),
                Arguments.of(
                        EventType.EXCHANGE_RATES.toString(),
                        "/exchangeRateEvents.json",
                        "/outExchangeRateEventsDto.json"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("get200WhenEventsExist")
    @DisplayName("get 200 when Events exist")
    public void get200WhenEventsExist(@Nonnull String eventType,
                                      @Nonnull String eventsLocation,
                                      @Nonnull String expectedEventsLocation) throws Exception {
        eventsRepository.saveAll(testHelpers.load(eventsLocation, new TypeReference<List<? extends Event>>() {}));

        this.mockMvc.perform(
                        get(EventsController.ENDPOINT + "/" + eventType)
                ).andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(testHelpers.load(expectedEventsLocation), true));
    }

    @Test
    @DisplayName("get 400 when creating new Event with Event type which doesn't exists")
    public void get400WhenCreatingEvenWithEventTypeWhichDoesntExists() throws Exception {
        this.mockMvc.perform(
                        post(EventsController.ENDPOINT + "/RATES")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(testHelpers.load("/inStockRateEventDto.json"))
                ).andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    public static Stream<Arguments> get201WhenCreatingEvent() {
        return Stream.of(
                Arguments.of(
                        EventType.STOCKS.toString(),
                        "/inStockRateEventDto.json",
                        "/outStockRateEventDto.json"
                ),
                Arguments.of(
                        EventType.EXCHANGE_RATES.toString(),
                        "/inExchangeRateEventDto.json",
                        "/outExchangeRateEventDto.json"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("get201WhenCreatingEvent")
    @DisplayName("get 201 when creating new Event")
    public void get201WhenCreatingEvent(@Nonnull String eventType,
                                        @Nonnull String inEventDtoLocation,
                                        @Nonnull String outEventDtoLocation) throws Exception {
        try (var uuidMock = Mockito.mockStatic(UUID.class); var dateMock = Mockito.mockStatic(Date.class)) {
            var expectedEvent = testHelpers.load(
                    outEventDtoLocation,
                    new TypeReference<OutEventDto<? extends EventPayload>>() {}
            );

            uuidMock.when(UUID::randomUUID).thenReturn(expectedEvent.getId());
            dateMock.when((MockedStatic.Verification) Date.from(any())).thenReturn(expectedEvent.getCreatedAt());

            this.mockMvc.perform(
                            post(EventsController.ENDPOINT + "/" + eventType)
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .content(testHelpers.load(inEventDtoLocation))
                    ).andDo(print())
                    .andExpect(status().is(HttpStatus.CREATED.value()))
                    .andExpect(content().json(testHelpers.load(outEventDtoLocation), true));
        }
    }

}
