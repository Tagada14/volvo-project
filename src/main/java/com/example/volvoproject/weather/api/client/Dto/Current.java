package com.example.volvoproject.weather.api.client.Dto;

import com.example.volvoproject.weather.api.client.deserializers.CurrentDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CurrentDeserializer.class)
public record Current(LocalDate last_updated) {
}
