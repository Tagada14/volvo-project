package com.example.volvoproject.weather.controller;

import com.example.volvoproject.weather.error.InvalidParameterException;
import com.example.volvoproject.weather.error.WeatherNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WeatherControllerAdvice {
    @ExceptionHandler(WeatherNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String weatherNotFoundHandler(WeatherNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidParameterException(InvalidParameterException ex) {
        return ex.getMessage();
    }
}
