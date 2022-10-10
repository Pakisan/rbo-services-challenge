package com.gihub.pakisan.pubsub.events.service;

import com.gihub.pakisan.pubsub.events.dto.InEventDto;
import com.gihub.pakisan.pubsub.events.dto.OutEventDto;
import com.gihub.pakisan.pubsub.events.dto.exchangerates.OutExchangeRateEventDto;
import com.gihub.pakisan.pubsub.events.dto.stocks.OutStockRateEventDto;
import com.gihub.pakisan.pubsub.events.repository.Event;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import com.gihub.pakisan.pubsub.exchangerates.ExchangeRate;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventsConverter {

    @Nonnull
    public Event convert(@Nonnull InEventDto event) {
        return new Event(
                UUID.randomUUID(),
                Date.from(Instant.now()),
                event.getType(),
                event.getPayload()
        );
    }

    @Nonnull
    public List<OutEventDto<? extends EventPayload>> convert(@Nonnull List<Event> events) {
        return events.stream()
                .map(this::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @CheckForNull
    public OutEventDto<? extends EventPayload> convert(@Nonnull Event event) {
        switch (event.getType()) {
            case STOCKS: return OutStockRateEventDto.builder()
                    .id(event.getId())
                    .createdAt(event.getCreatedAt())
                    .payload((StockRate) event.getPayload())
                    .build();
            case EXCHANGE_RATES: return OutExchangeRateEventDto.builder()
                    .id(event.getId())
                    .createdAt(event.getCreatedAt())
                    .payload((ExchangeRate) event.getPayload())
                    .build();
            default: return null;
        }
    }

}
