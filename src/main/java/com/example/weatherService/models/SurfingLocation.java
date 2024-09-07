package com.example.weatherService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurfingLocation {

  private Location location;
  private WeatherCondition weatherCondition;
}
