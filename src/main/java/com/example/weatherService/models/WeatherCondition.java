package com.example.weatherService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherCondition {

  private double temperature;
  private double windSpeed;
}
