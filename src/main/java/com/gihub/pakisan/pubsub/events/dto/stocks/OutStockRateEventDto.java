package com.gihub.pakisan.pubsub.events.dto.stocks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gihub.pakisan.pubsub.events.dto.OutEventDto;
import com.gihub.pakisan.pubsub.stocks.StockRate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutStockRateEventDto extends OutEventDto<StockRate> {
}
