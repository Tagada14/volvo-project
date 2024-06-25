package com.example.volvoproject.weather.error;

public class WeatherNotFoundException extends RuntimeException {
    public WeatherNotFoundException(String city) {
        super("Could not provide weather for city: " + city);
    }
}
