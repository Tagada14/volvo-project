package com.example.volvoproject.weather.api.client;

import com.example.volvoproject.weather.api.client.Dto.Weather;
import com.example.volvoproject.weather.error.WeatherNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiClient {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherApiClient.class);
    private final RestTemplate restTemplate;
    @Value("${api.key}")
    private String apiKey;

    public WeatherApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Weather getWeather(String city, Integer days) throws WeatherNotFoundException {
        try {
            return restTemplate.getForObject(
                    "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + city + "&days=" +
                            days.toString() + "&aqi=no&alerts=no\n", Weather.class);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new WeatherNotFoundException(city);
        }
    }
}
