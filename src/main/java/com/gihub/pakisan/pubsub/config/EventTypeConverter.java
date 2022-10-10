package com.gihub.pakisan.pubsub.config;

import com.gihub.pakisan.pubsub.events.repository.EventType;
import org.springframework.core.convert.converter.Converter;

public class EventTypeConverter implements Converter<String, EventType> {

    @Override
    public EventType convert(String source) {
        try {
            return EventType.valueOf(source.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
