package com.example.volvoproject.weather.service;

import com.example.volvoproject.weather.api.client.Dto.Weather;
import com.example.volvoproject.weather.repository.WeatherForecast;
import com.example.volvoproject.weather.repository.WeatherForecastRepository;
import com.example.volvoproject.weather.error.WeatherNotFoundException;
import com.example.volvoproject.weather.api.client.WeatherApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class WeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherService.class);
    private final WeatherApiClient weatherApiClient;
    private final WeatherForecastRepository weatherForecastRepository;

    public WeatherService(WeatherApiClient weatherApiClient, WeatherForecastRepository weatherForecastRepository) {
        this.weatherApiClient = weatherApiClient;
        this.weatherForecastRepository = weatherForecastRepository;
    }

    public List<WeatherForecast> getWeatherForecast(String city, int days, boolean invalidateCache) throws WeatherNotFoundException {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(days - 1);
        List<WeatherForecast> savedWeatherForecasts = weatherForecastRepository.findByForecastDateBetweenAndCity(from, to, city);
        if (savedWeatherForecasts.size() < days || invalidateCache || savedWeatherForecasts.stream().anyMatch(wf -> wf.getLast_updated().isBefore(from))) {
            LOG.info("Fetching forecast for {}", city);
            if (!savedWeatherForecasts.isEmpty())
                weatherForecastRepository.deleteAllById(savedWeatherForecasts.stream().map(WeatherForecast::getId).toList());
            Weather weather = weatherApiClient.getWeather(city, days);
            List<WeatherForecast> weatherForecasts = WeatherForecast.WeatherForecastMapper(weather);
            weatherForecastRepository.saveAll(weatherForecasts);
            return weatherForecasts;
        }
        else {
            LOG.info("Returning cached forecast for {}", city);
            return savedWeatherForecasts;
        }

    }
}
