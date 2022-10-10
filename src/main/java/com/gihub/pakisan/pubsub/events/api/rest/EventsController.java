package com.gihub.pakisan.pubsub.events.api.rest;

import com.gihub.pakisan.pubsub.events.dto.InEventDto;
import com.gihub.pakisan.pubsub.events.dto.OutEventDto;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import com.gihub.pakisan.pubsub.events.repository.EventType;
import com.gihub.pakisan.pubsub.events.service.EventsConverter;
import com.gihub.pakisan.pubsub.events.service.EventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.CheckForNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(EventsController.ENDPOINT)
public class EventsController {

    public final static String ENDPOINT = "/api/protected/v1/events";

    private final EventsService eventsService;
    private final EventsConverter eventsConverter;

    @GetMapping(
            path = "/{eventType}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<List<OutEventDto<? extends EventPayload>>> get(@PathVariable(required = false) @CheckForNull EventType eventType) {
        var events = eventsService.find(eventType);
        if (events == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventsConverter.convert(events));
    }

    @PostMapping(
            path = "/{eventType}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<OutEventDto<? extends EventPayload>> create(@PathVariable(required = false) @CheckForNull EventType eventType,
                                                                      @RequestBody InEventDto event) {
        var createdEvent = eventsService.save(eventType, eventsConverter.convert(event));
        if (createdEvent == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventsConverter.convert(createdEvent));
    }

}
