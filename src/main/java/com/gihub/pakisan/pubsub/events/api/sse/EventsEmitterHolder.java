package com.gihub.pakisan.pubsub.events.api.sse;

import com.gihub.pakisan.pubsub.events.repository.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EventsEmitterHolder {

    private static final Map<EventType, List<SseEmitter>> sseEmitter = new ConcurrentHashMap<>();

    public SseEmitter hold(@Nonnull EventType eventType) {
        var emitter = new SseEmitter(1_000_000L);
        emitter.onCompletion(() -> sseEmitter.get(eventType).remove(emitter));

        if (sseEmitter.containsKey(eventType)) {
            sseEmitter.get(eventType).add(emitter);
        } else {
            sseEmitter.put(eventType, new LinkedList<>(List.of(emitter)));
        }

        return emitter;
    }

    public void broadcast(@Nonnull EventType eventType, @Nonnull String payload) {
        sseEmitter.getOrDefault(eventType, new LinkedList<>()).forEach(emitter -> {
            var event = SseEmitter.event()
                    .data(payload)
                    .name(eventType.name());
            try {
                emitter.send(event);
            } catch (IOException e) {
                log.error("broadcast error.", e);
            }
        });
    }

}
