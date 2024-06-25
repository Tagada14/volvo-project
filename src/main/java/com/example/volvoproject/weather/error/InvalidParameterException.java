package com.example.volvoproject.weather.error;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
        super("The number of days must be greater than zero");
    }
}
