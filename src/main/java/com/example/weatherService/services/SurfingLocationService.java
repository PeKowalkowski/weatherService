package com.example.weatherService.services;

import com.example.weatherService.models.Location;
import com.example.weatherService.models.SurfingLocation;

import java.util.List;

public interface SurfingLocationService {

  SurfingLocation findBestSurfingLocation(List<Location> locations, String date);

  boolean isWeatherSuitableForSurfing(SurfingLocation surfingLocation);

  double calculateSurfingScore(SurfingLocation surfingLocation);


}
