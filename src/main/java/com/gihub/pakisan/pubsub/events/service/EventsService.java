package com.gihub.pakisan.pubsub.events.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gihub.pakisan.pubsub.events.repository.Event;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import com.gihub.pakisan.pubsub.events.repository.EventType;
import com.gihub.pakisan.pubsub.events.repository.EventsRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventsRepository eventsRepository;

    @CheckForNull
    @Contract("null -> null; !null -> !null")
    public List<Event> find(@CheckForNull EventType eventType) {
        if (eventType == null) {
            return null;
        }

        return eventsRepository.findAllByType(eventType);
    }

    @CheckForNull
    @Contract("null, _ -> null; !null, _ -> !null")
    public Event save(@CheckForNull EventType eventType, @Nonnull Event event) {
        if (eventType == null) {
            return null;
        }

        return eventsRepository.save(event);
    }

}
