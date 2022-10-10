package com.gihub.pakisan.pubsub.events.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.gihub.pakisan.pubsub.events.repository.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutEventDto<PayloadType extends EventPayload> {

    @JsonProperty(value = "id", required = true)
    @JsonPropertyDescription("ID")
    private UUID id;

    @JsonProperty(value = "createdAt", required = true)
    @JsonPropertyDescription("created at")
    private Date createdAt;

    @JsonProperty(value = "payload", required = true)
    @JsonPropertyDescription("payload")
    private PayloadType payload;

}
