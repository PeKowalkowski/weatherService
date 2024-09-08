package com.example.weatherService.servicesImpl;

import com.example.weatherService.entities.LocationEntity;
import com.example.weatherService.models.Location;
import com.example.weatherService.models.LocationRequest;
import com.example.weatherService.models.SurfingLocation;
import com.example.weatherService.models.WeatherCondition;
import com.example.weatherService.repositories.LocationRepo;
import com.example.weatherService.services.SurfingLocationService;
import com.example.weatherService.services.WeatherApiClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurfingLocationServiceImpl implements SurfingLocationService {

  private final WeatherApiClientServiceImpl weatherApiClientService;
  private final LocationRepo locationRepo;


  @Value("${weather.api.key}")
  private String apiKey;
  @Value("${weather.api.base-url}")
  private String baseUrl;

  @Override
  public SurfingLocation findBestSurfingLocation(String date) {

    List<LocationEntity> locationEntities = locationRepo.findAll();
    return locationEntities.stream()
      .map(locationEntity -> {
        Location location = new Location(locationEntity.getName());
        WeatherCondition weather = weatherApiClientService.getWeatherForLocation(location, date);
        return new SurfingLocation(location, weather);
      })
      .filter(this::isWeatherSuitableForSurfing)
      .max(Comparator.comparing(this::calculateSurfingScore))
      .orElse(null);
  }


  @Override
  public boolean isWeatherSuitableForSurfing(SurfingLocation surfingLocation) {
    WeatherCondition weather = surfingLocation.getWeatherCondition();
    return weather.getWindSpeed() >= 5 && weather.getWindSpeed() <= 18 &&
      weather.getTemperature() >= 5 && weather.getTemperature() <= 35;
  }

  @Override
  public double calculateSurfingScore(SurfingLocation surfingLocation) {
    WeatherCondition weather = surfingLocation.getWeatherCondition();
    return (int) (weather.getWindSpeed() * 3 + weather.getTemperature());
  }

  @Override
  public void addLocation(String city, String country) {
    String url = String.format(baseUrl, city, country, apiKey);
    LocationEntity locationEntity = new LocationEntity();
    locationEntity.setName(city);
    locationEntity.setUrl(url);
    locationRepo.save(locationEntity);
  }

  @Override
  public void addLocations(List<LocationRequest> locationRequests) {
    List<LocationEntity> locationEntities = locationRequests.stream()
      .map(locationRequest -> {
        String url = String.format(baseUrl, locationRequest.getCity(), locationRequest.getCountry(), apiKey);
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setName(locationRequest.getCity());
        locationEntity.setUrl(url);
        return locationEntity;
      })
      .collect(Collectors.toList());

    locationRepo.saveAll(locationEntities);
  }
}
