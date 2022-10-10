package com.gihub.pakisan.pubsub.stocks;

import com.fasterxml.jackson.annotation.*;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonClassDescription("Stock rate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockRate extends EventPayload {

    @Nonnull
    @JsonProperty(value = "index", required = true)
    @JsonPropertyDescription("index")
    private String index;

    @Nonnull
    @JsonProperty(value = "dateTime", required = true)
    @JsonPropertyDescription("date and time")
    private Date dateTime;

    @CheckForNull
    @JsonProperty(value = "price", required = true)
    @JsonPropertyDescription("price")
    private Double price;

    @CheckForNull
    @JsonProperty(value = "change", required = true)
    @JsonPropertyDescription("price change")
    private Double change;

}
