package com.example.weatherService.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
  private String name;
  private double latitude;
  private double longitude;

}
