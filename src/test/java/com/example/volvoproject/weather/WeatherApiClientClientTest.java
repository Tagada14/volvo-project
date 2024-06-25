package com.example.volvoproject.weather;

import com.example.volvoproject.weather.api.client.Dto.*;
import com.example.volvoproject.weather.api.client.WeatherApiClient;
import com.example.volvoproject.weather.error.WeatherNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class WeatherApiClientClientTest {

    LocalDate mock_date = LocalDate.of(2024, 6, 25);
    Weather mock_api_response = new Weather(new Location("Wroclaw"), new Current(mock_date), new Forecast(
            List.of(new ForecastDay(mock_date, new Day(27.5, 12.3, 20.3, 24.8, 0.0, 0.0, 10.0, 63, 7.0)))));


    @Mock
    private RestTemplate mockRestTemplate;

    @Value("${api.key}")
    private String mockApiKey;

    @InjectMocks
    private WeatherApiClient weatherApiClient;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(WeatherServiceTest.class);
    }

    @Test
    // When external API return the forecast we deserialize it and pass it to the service.
    public void whenExternalApiReturnsValue_ThenReturnValue() {
        String city = "Wroclaw";
        int days = 1;
        Mockito.when(this.mockRestTemplate.getForObject(
                "http://api.weatherapi.com/v1/forecast.json?key=" + mockApiKey + "&q=" + city + "&days=" + days +
                        "&aqi=no&alerts=no\n", Weather.class)).thenReturn(this.mock_api_response);

        this.weatherApiClient.getWeather(city, days);

        Mockito.verify(this.mockRestTemplate).getForObject(
                "http://api.weatherapi.com/v1/forecast.json?key=" + mockApiKey + "&q=" + city + "&days=" + days +
                        "&aqi=no&alerts=no\n", Weather.class);

    }

    @Test
    // When the external API returns an error we propagate it to the service.
    public void whenExternalApiReturnsError_ThenReturnError() {
        String city = "Wroclaw";
        int days = 1;
        Mockito.when(this.mockRestTemplate.getForObject(
                "http://api.weatherapi.com/v1/forecast.json?key=" + mockApiKey + "&q=" + city + "&days=" + days +
                        "&aqi=no&alerts=no\n", Weather.class)).thenThrow(new RestClientException(""));
        assertThrows(WeatherNotFoundException.class, () -> this.weatherApiClient.getWeather(city, days));

        Mockito.verify(this.mockRestTemplate).getForObject(
                "http://api.weatherapi.com/v1/forecast.json?key=" + mockApiKey + "&q=" + city + "&days=" + days +
                        "&aqi=no&alerts=no\n", Weather.class);

    }

    @Test
    // Confirm that the deserialization works correctly.
    public void confirmDeserializerWorksCorrectly() throws IOException {
        ClassPathResource resource = new ClassPathResource("/mock_api_response.json", WeatherApiClientClientTest.class);
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.readTree(inputStream);
        Weather weather = mapper.readerFor(Weather.class).readValue(node);

        assert (weather.equals(mock_api_response));
    }
}
