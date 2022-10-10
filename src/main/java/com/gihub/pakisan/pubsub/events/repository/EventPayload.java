package com.gihub.pakisan.pubsub.events.repository;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonClassDescription("Event payload")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPayload {
}
