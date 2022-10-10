package com.gihub.pakisan.pubsub.events.dto;

import com.fasterxml.jackson.annotation.*;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import com.gihub.pakisan.pubsub.events.repository.EventType;
import com.gihub.pakisan.pubsub.exchangerates.ExchangeRate;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InEventDto {

    @JsonProperty(value = "type", required = true)
    @JsonPropertyDescription("kind of event type")
    private EventType type;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = StockRate.class, name = "STOCKS"),
            @JsonSubTypes.Type(value = ExchangeRate.class, name = "EXCHANGE_RATES")}
    )
    @JsonProperty(value = "payload", required = true)
    @JsonPropertyDescription("payload")
    private EventPayload payload;

}
