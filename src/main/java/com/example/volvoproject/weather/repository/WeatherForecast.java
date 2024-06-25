package com.example.volvoproject.weather.repository;

import com.example.volvoproject.weather.api.client.Dto.ForecastDay;
import com.example.volvoproject.weather.api.client.Dto.Weather;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

@Entity
public class WeatherForecast {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate last_updated;
    private String city;
    private LocalDate forecast_date;
    private Double max_temp_c;
    private Double min_temp_c;
    private Double avg_temp_c;
    private Double max_wind_kph;
    private Double total_precip_mm;
    private Double total_snow_cm;
    private Double avg_vis_km;
    private int avg_humidity;
    private Double uv;

    public WeatherForecast() {
    }

    public WeatherForecast(String city, LocalDate last_updated, LocalDate forecast_date, Double max_temp_c,
                           Double min_temp_c, Double avg_temp_c, Double max_wind_kph,
                           Double total_precip_mm, Double total_snow_cm,
                           Double avg_vis_km, int avg_humidity, double uv) {
        this.city = city;
        this.last_updated = last_updated;
        this.forecast_date = forecast_date;
        this.max_temp_c = max_temp_c;
        this.min_temp_c = min_temp_c;
        this.avg_temp_c = avg_temp_c;
        this.max_wind_kph = max_wind_kph;
        this.total_precip_mm = total_precip_mm;
        this.total_snow_cm = total_snow_cm;
        this.avg_vis_km = avg_vis_km;
        this.avg_humidity = avg_humidity;
        this.uv = uv;
    }

    public static List<WeatherForecast> WeatherForecastMapper(Weather weather) {
        String city = weather.location().name();
        LocalDate last_updated = weather.current().last_updated();
        return weather.forecast().forecastDays().stream().map((ForecastDay forecastDay) -> new WeatherForecast(city,
                last_updated, forecastDay.date(), forecastDay.day().maxtemp_c(), forecastDay.day().mintemp_c(),
                forecastDay.day().avgtemp_c(), forecastDay.day().maxwind_kph(), forecastDay.day().totalprecip_mm(),
                forecastDay.day().totalsnow_cm(), forecastDay.day().avgvis_km(), forecastDay.day().avghumidity(),
                forecastDay.day().uv())
        ).toList();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getForecast_date() {
        return forecast_date;
    }

    public void setForecast_date(LocalDate day) {
        this.forecast_date = day;
    }

    public Double getMax_temp_c() {
        return max_temp_c;
    }

    public void setMax_temp_c(Double max_temp_c) {
        this.max_temp_c = max_temp_c;
    }

    public Double getMin_temp_c() {
        return min_temp_c;
    }

    public void setMin_temp_c(Double min_temp_c) {
        this.min_temp_c = min_temp_c;
    }

    public Double getAvg_temp_c() {
        return avg_temp_c;
    }

    public void setAvg_temp_c(Double avg_temp_c) {
        this.avg_temp_c = avg_temp_c;
    }

    public Double getMax_wind_kph() {
        return max_wind_kph;
    }

    public void setMax_wind_kph(Double max_wind_kph) {
        this.max_wind_kph = max_wind_kph;
    }

    public Double getTotal_precip_mm() {
        return total_precip_mm;
    }

    public void setTotal_precip_mm(Double total_precip_mm) {
        this.total_precip_mm = total_precip_mm;
    }

    public Double getTotal_snow_cm() {
        return total_snow_cm;
    }

    public void setTotal_snow_cm(Double total_snow_cm) {
        this.total_snow_cm = total_snow_cm;
    }

    public Double getAvg_vis_km() {
        return avg_vis_km;
    }

    public void setAvg_vis_km(Double avg_vis_km) {
        this.avg_vis_km = avg_vis_km;
    }

    public int getAvg_humidity() {
        return avg_humidity;
    }

    public void setAvg_humidity(int avg_humidity) {
        this.avg_humidity = avg_humidity;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }


    public LocalDate getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(LocalDate last_updated) {
        this.last_updated = last_updated;
    }
}
