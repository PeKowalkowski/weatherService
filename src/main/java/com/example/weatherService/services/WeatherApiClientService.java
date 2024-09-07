package com.example.weatherService.services;

import com.example.weatherService.models.Location;
import com.example.weatherService.models.WeatherCondition;

public interface WeatherApiClientService {
  WeatherCondition getWeatherForLocation(Location location, String date);
}
