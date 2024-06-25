package com.example.volvoproject.weather;

import com.example.volvoproject.weather.api.client.Dto.*;
import com.example.volvoproject.weather.api.client.WeatherApiClient;
import com.example.volvoproject.weather.error.WeatherNotFoundException;
import com.example.volvoproject.weather.repository.WeatherForecast;
import com.example.volvoproject.weather.repository.WeatherForecastRepository;
import com.example.volvoproject.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    LocalDate mock_date = LocalDate.now();
    Weather mock_api_response = new Weather(new Location("Wroclaw"), new Current(mock_date), new Forecast(
            List.of(new ForecastDay(mock_date, new Day(27.5, 12.3, 20.3, 15.4, 0.0, 0.0, 10.0, 63, 7.0)))));
    @Mock
    private WeatherApiClient mockWeatherApiClient;

    @Mock
    private WeatherForecastRepository mockWeatherForecastRepository;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(WeatherServiceTest.class);
    }
//  When the requested forecast is in cache return it without calling external API.
    @Test
    public void whenForecastInCache_ThenReturnCachedForecast() {
        String city = "Wroclaw";
        int days = 1;
        Mockito.when(this.mockWeatherForecastRepository.findByForecastDateBetweenAndCity(mock_date, mock_date, city))
                .thenReturn(WeatherForecast.WeatherForecastMapper(mock_api_response));

        this.weatherService.getWeatherForecast(city, days, false);

        Mockito.verify(this.mockWeatherApiClient, Mockito.never()).getWeather(city, days);
        Mockito.verify(this.mockWeatherForecastRepository, Mockito.never()).saveAll(Mockito.anyCollection());

    }
//  When the cache doesn't have the requested forecast then request new forecast.
    @Test
    public void whenCacheEmpty_ThenRequestNewForecast() {
        String city = "Wroclaw";
        int days = 1;
        Mockito.when(this.mockWeatherApiClient.getWeather(city, days)).thenReturn(this.mock_api_response);
        Mockito.when(this.mockWeatherForecastRepository.findByForecastDateBetweenAndCity(mock_date, mock_date, city))
                .thenReturn(List.of());
        Mockito.when(this.mockWeatherForecastRepository.saveAll(Mockito.anyCollection())).thenReturn(List.of());

        this.weatherService.getWeatherForecast(city, days, false);

        Mockito.verify(this.mockWeatherApiClient).getWeather(city, days);
        Mockito.verify(this.mockWeatherForecastRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());

    }
//  When the request contains cache invalidation flag then we ignore the cache and request new forecast.
    @Test
    public void whenCacheInvalidationRequested_ThenRequestNewForecast() {
        String city = "Wroclaw";
        int days = 1;
        Mockito.when(this.mockWeatherApiClient.getWeather(city, days)).thenReturn(this.mock_api_response);
        Mockito.when(this.mockWeatherForecastRepository.findByForecastDateBetweenAndCity(mock_date, mock_date, city))
                .thenReturn(WeatherForecast.WeatherForecastMapper(mock_api_response));
        Mockito.when(this.mockWeatherForecastRepository.saveAll(Mockito.anyCollection())).thenReturn(List.of());

        this.weatherService.getWeatherForecast(city, days, true);

        Mockito.verify(this.mockWeatherApiClient).getWeather(city, days);
        Mockito.verify(this.mockWeatherForecastRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());

    }
//  When the forecast in the cache is old (not from today) then we request new forecast.
    @Test
    public void whenForecastInCacheIsOld_ThenRequestNewForecast() {
        String city = "Wroclaw";
        int days = 1;
        Weather mock_api_response_outdated = new Weather(new Location("Wroclaw"), new Current(mock_date.minusDays(1)),
                new Forecast(
                        List.of(new ForecastDay(mock_date, new Day(27.5, 12.3, 20.3, 15.4, 0.0, 0.0, 10.0, 63, 7.0)))));
        Mockito.when(this.mockWeatherApiClient.getWeather(city, days)).thenReturn(this.mock_api_response);
        Mockito.when(this.mockWeatherForecastRepository.findByForecastDateBetweenAndCity(mock_date, mock_date, city))
                .thenReturn(WeatherForecast.WeatherForecastMapper(mock_api_response_outdated));
        Mockito.when(this.mockWeatherForecastRepository.saveAll(Mockito.anyCollection())).thenReturn(List.of());

        this.weatherService.getWeatherForecast(city, days, false);

        Mockito.verify(this.mockWeatherApiClient).getWeather(city, days);
        Mockito.verify(this.mockWeatherForecastRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());

    }
//  When the forecast we have in cache is only partial eg. 2 out of 3 requested days then we request new forecast.
    @Test
    public void whenForecastInCacheIsOnlyPartial_ThenRequestNewForecast() {
        String city = "Wroclaw";
        int days = 2;
        Weather mock_api_response_longer = new Weather(new Location("Wroclaw"), new Current(mock_date), new Forecast(
                List.of(new ForecastDay(mock_date, new Day(27.5, 12.3, 20.3, 15.4, 0.0, 0.0, 10.0, 63, 7.0)),
                        new ForecastDay(mock_date.plusDays(1),
                                new Day(27.5, 12.3, 20.3, 15.4, 0.0, 0.0, 10.0, 63, 7.0)))));
        Mockito.when(this.mockWeatherApiClient.getWeather(city, days)).thenReturn(mock_api_response_longer);
        Mockito.when(this.mockWeatherForecastRepository.findByForecastDateBetweenAndCity(mock_date,
                        mock_date.plusDays(days - 1), city))
                .thenReturn(WeatherForecast.WeatherForecastMapper(mock_api_response));
        Mockito.when(this.mockWeatherForecastRepository.saveAll(Mockito.anyCollection())).thenReturn(List.of());

        this.weatherService.getWeatherForecast(city, days, false);

        Mockito.verify(this.mockWeatherApiClient).getWeather(city, days);
        Mockito.verify(this.mockWeatherForecastRepository, Mockito.times(1)).saveAll(Mockito.anyCollection());
        Mockito.verify(this.mockWeatherForecastRepository, Mockito.times(1)).deleteAllById(Mockito.anyCollection());

    }
//  Test that our Service propagates the error when the Api Client throws an error.
    @Test
    public void whenWeatherApiClientThrowsError_ThenWeatherServiceThrowsError() {
        String city = "Wroclaw";
        int days = 1;
        Mockito.when(this.mockWeatherApiClient.getWeather(city, days)).thenThrow(new WeatherNotFoundException(city));
        assertThrows(WeatherNotFoundException.class, () -> this.weatherService.getWeatherForecast(city, days, false));

        Mockito.verify(this.mockWeatherApiClient).getWeather(city, days);
    }
}
