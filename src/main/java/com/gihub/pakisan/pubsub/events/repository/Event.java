package com.gihub.pakisan.pubsub.events.repository;

import com.fasterxml.jackson.annotation.*;
import com.gihub.pakisan.pubsub.exchangerates.ExchangeRate;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "event")
@Table(name = "events")
//@TypeDef(
//        name = "json",
//        typeClass = JsonType.class
//)
@JsonClassDescription("Event")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    @Id
    @Nonnull
    @JsonProperty(value = "id", required = true)
    @JsonPropertyDescription("id")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Nonnull
    @JsonAlias("created_at")
    @JsonProperty(value = "createdAt", required = true)
    @JsonPropertyDescription("when event was received")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Nonnull
    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "type", required = true)
    @JsonPropertyDescription("kind of event")
    @Column(name = "type", nullable = false, updatable = false)
    private EventType type;

    @Nonnull
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
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
