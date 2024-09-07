package com.example.weatherService.controllersImpl;

import com.example.weatherService.controllers.SurfingLocationController;
import com.example.weatherService.models.Location;
import com.example.weatherService.models.SurfingLocation;
import com.example.weatherService.responses.SurfingLocationResponse;
import com.example.weatherService.servicesImpl.SurfingLocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SurfingLocationControllerImpl implements SurfingLocationController {

  private final SurfingLocationServiceImpl surfingLocationService;

  @Override
  public ResponseEntity<SurfingLocationResponse> getBestSurfingLocation(List<Location> locations, String date) {
    try {
      SurfingLocation bestLocation = surfingLocationService.findBestSurfingLocation(locations, date);
      if (bestLocation != null) {
        SurfingLocationResponse response = new SurfingLocationResponse(
          bestLocation.getLocation().getName(),
          bestLocation.getWeatherCondition().getTemperature(),
          bestLocation.getWeatherCondition().getWindSpeed()
        );
        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new SurfingLocationResponse("Error finding the best surfing location: " + e.getMessage()));
    }
  }
}

