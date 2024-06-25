package com.example.volvoproject.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    @Query("select wf from WeatherForecast wf where wf.last_updated = ?1 and wf.forecast_date between ?1 and ?2 and wf.city = ?3")
    List<WeatherForecast> findByForecastDateBetweenAndCity(LocalDate from, LocalDate to, String city);
}
