package com.example.volvoproject.weather.controller;

import com.example.volvoproject.weather.error.InvalidParameterException;
import com.example.volvoproject.weather.service.WeatherService;
import com.example.volvoproject.weather.repository.WeatherForecast;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public CollectionModel<WeatherForecast> getWeather(
            @RequestParam(value = "city") String city,
            @RequestParam(value = "days", defaultValue = "1") int days,
            @RequestParam(value = "invalidate_cache", defaultValue = "false") Boolean invalidateCache) {
        if (days < 1) throw new InvalidParameterException();
        List<WeatherForecast> weatherForecast = weatherService.getWeatherForecast(city, days, invalidateCache);
        return CollectionModel.of(weatherForecast,
                linkTo(methodOn(WeatherController.class).getWeather(weatherForecast.getFirst().getCity(),
                        weatherForecast.size(), invalidateCache)).withSelfRel());
    }
}
