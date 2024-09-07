package com.example.weatherService.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor public class SurfingLocationResponse {
  private String locationName;
  private double temperature;
  private double windSpeed;
  private String errorMessage;

  public SurfingLocationResponse(String locationName, double temperature, double windSpeed) {
    this.locationName = locationName;
    this.temperature = temperature;
    this.windSpeed = windSpeed;
    this.errorMessage = null;
  }

  public SurfingLocationResponse(String errorMessage) {
    this.errorMessage = errorMessage;
    this.locationName = null;
    this.temperature = 0;
    this.windSpeed = 0;
  }
}
