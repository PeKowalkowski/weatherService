package com.example.weatherService.servicesImpl;

import com.example.weatherService.models.Location;
import com.example.weatherService.models.WeatherCondition;
import com.example.weatherService.services.WeatherApiClientService;
import com.example.weatherService.utils.LocationUrls;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherApiClientServiceImpl implements WeatherApiClientService {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final LocationUrls locationUrls;

  @Value("${weather.api.key}")
  private String apiKey;



  @Override
  public WeatherCondition getWeatherForLocation(Location location, String date) {
    String locationKey = locationUrls.LOCATION_URLS.entrySet().stream()
      .filter(entry -> location.getName().equalsIgnoreCase(entry.getKey()))
      .map(Map.Entry::getValue)
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unknown location: " + location.getName()));


    String url = String.format(locationKey, apiKey);


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
