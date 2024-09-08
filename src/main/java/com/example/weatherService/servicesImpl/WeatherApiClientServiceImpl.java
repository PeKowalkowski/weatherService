package com.example.weatherService.servicesImpl;

import com.example.weatherService.entities.LocationEntity;
import com.example.weatherService.models.Location;
import com.example.weatherService.models.WeatherCondition;
import com.example.weatherService.repositories.LocationRepo;
import com.example.weatherService.services.WeatherApiClientService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherApiClientServiceImpl implements WeatherApiClientService {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final LocationRepo locationRepo;



  @Value("${weather.api.key}")
  private String apiKey;


  @Override
  public WeatherCondition getWeatherForLocation(Location location, String date) {
    LocationEntity locationEntity = locationRepo.findByNameIgnoreCase(location.getName())
      .orElseThrow(() -> new IllegalArgumentException("Unknown location: " + location.getName()));

    String url = String.format(locationEntity.getUrl(), apiKey);
    String response = restTemplate.getForObject(url, String.class);

    try {
      JsonNode jsonResponse = objectMapper.readTree(response);
      double temperature = jsonResponse.path("data").get(0).path("temp").asDouble();
      double windSpeed = jsonResponse.path("data").get(0).path("wind_spd").asDouble();
      return new WeatherCondition(temperature, windSpeed);
    } catch (Exception e) {
      throw new RuntimeException("Error parsing weather response", e);
    }
  }
}
