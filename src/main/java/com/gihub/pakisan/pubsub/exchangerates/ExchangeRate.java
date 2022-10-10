package com.gihub.pakisan.pubsub.exchangerates;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
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
@JsonClassDescription("Exchange rate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate extends EventPayload {

    @Nonnull
    @JsonProperty(value = "bank", required = true)
    @JsonPropertyDescription("bank name")
    private String bank;

    @Nonnull
    @JsonProperty(value = "dateTime", required = true)
    @JsonPropertyDescription("currency rate date and time")
    private Date dateTime;

    @Nonnull
    @JsonProperty(value = "currency", required = true)
    @JsonPropertyDescription("currency to exchange ISO 4217")
    private String currency;

    @CheckForNull
    @JsonProperty(value = "buy", required = true)
    @JsonPropertyDescription("buy")
    private Long buy;

    @CheckForNull
    @JsonProperty(value = "sell", required = true)
    @JsonPropertyDescription("sell")
    private Long sell;

}
