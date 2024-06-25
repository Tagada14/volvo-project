package com.example.volvoproject.weather.repository;

import com.example.volvoproject.weather.api.client.Dto.Weather;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@Configuration
class LoadDatabase {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(WeatherForecastRepository repository) {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<String> paths = List.of("/forecastsWroclaw.json", "/forecastsWarsaw.json", "/forecastsLodz.json",
                "/forecastsCracow.json", "/forecastsPoznan.json");
        LocalDate preloadedDate = LocalDate.of(2024, 6, 25);
        List<List<WeatherForecast>> forecasts =
                paths.stream().map(path -> {
                            try {
                                return WeatherForecast.WeatherForecastMapper(
                                        mapper.readerFor(Weather.class).readValue(
                                                mapper.readTree(
                                                        new ClassPathResource(path, LoadDatabase.class).getInputStream())));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .map(xs -> xs.stream().peek(x -> {
                            x.setLast_updated(
                                    LocalDate.now());
                            x.setForecast_date(
                                    LocalDate.now().plusDays(DAYS.between(preloadedDate, x.getForecast_date())));
                        }).toList()).toList();

        return args ->

        {
            forecasts.forEach(forecast -> {
                LOG.info("Preloading forecast for {} {}",
                        forecast.getFirst().getCity(),
                        repository.saveAll(forecast));
            });
        };
    }
}
