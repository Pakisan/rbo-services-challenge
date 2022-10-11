package com.gihub.pakisan.pubsub.events.api.sse;

import com.gihub.pakisan.pubsub.events.repository.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.CheckForNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventsEmitter.ENDPOINT)
public class EventsEmitter {

    public final static String ENDPOINT = "/api/protected/v1/events";

    private final EventsEmitterHolder eventsEmitterHolder;

    @GetMapping(
            value = "/{eventType}/subscribe",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter subscribe(@PathVariable(required = false) @CheckForNull EventType eventType) {
        if (eventType == null) {
            return null;
        }

        return eventsEmitterHolder.hold(eventType);
    }

}
