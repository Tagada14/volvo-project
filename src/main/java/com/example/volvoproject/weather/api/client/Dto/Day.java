package com.example.volvoproject.weather.api.client.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Day(Double maxtemp_c, Double mintemp_c, Double avgtemp_c, Double maxwind_kph,
                  Double totalprecip_mm, Double totalsnow_cm, Double avgvis_km, int avghumidity, double uv) {
}
