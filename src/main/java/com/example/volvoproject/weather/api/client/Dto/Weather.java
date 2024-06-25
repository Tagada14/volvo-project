package com.example.volvoproject.weather.api.client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Weather(Location location, Current current, Forecast forecast) {
}
