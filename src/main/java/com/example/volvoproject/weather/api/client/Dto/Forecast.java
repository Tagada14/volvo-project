package com.example.volvoproject.weather.api.client.Dto;

import com.example.volvoproject.weather.api.client.deserializers.ForecastDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ForecastDeserializer.class)
public record Forecast(List<ForecastDay> forecastDays) {
}