package com.gihub.pakisan.pubsub.events.dto.exchangerates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gihub.pakisan.pubsub.events.dto.OutEventDto;
import com.gihub.pakisan.pubsub.exchangerates.ExchangeRate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutExchangeRateEventDto extends OutEventDto<ExchangeRate> {
}
