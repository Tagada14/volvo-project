package com.example.volvoproject.weather.api.client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastDay(LocalDate date, Day day) {
}
