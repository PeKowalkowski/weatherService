package com.example.weatherService.servicesImpl;

import com.example.weatherService.models.Location;
import com.example.weatherService.models.SurfingLocation;
import com.example.weatherService.models.WeatherCondition;
import com.example.weatherService.services.SurfingLocationService;
import com.example.weatherService.services.WeatherApiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurfingLocationServiceImpl implements SurfingLocationService {

  private final WeatherApiClientServiceImpl weatherApiClientService;
  @Override
  public SurfingLocation findBestSurfingLocation(List<Location> locations, String date) {
    return locations.stream()
      .map(location -> {
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
}
