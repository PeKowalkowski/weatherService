package com.example.weatherService.services;

import com.example.weatherService.models.Location;
import com.example.weatherService.models.LocationRequest;
import com.example.weatherService.models.SurfingLocation;

import java.util.List;

public interface SurfingLocationService {

  SurfingLocation findBestSurfingLocation(String date);

  boolean isWeatherSuitableForSurfing(SurfingLocation surfingLocation);

  double calculateSurfingScore(SurfingLocation surfingLocation);

  void addLocation(String city, String country);

  public void addLocations(List<LocationRequest> locationRequests);


}
