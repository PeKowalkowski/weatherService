package com.example.weatherService.controllers;

import com.example.weatherService.models.Location;
import com.example.weatherService.models.LocationRequest;
import com.example.weatherService.responses.SurfingLocationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/surfing")
public interface SurfingLocationController {

  @GetMapping("/best-location")
  ResponseEntity<SurfingLocationResponse> getBestSurfingLocation(String date);
  @PostMapping("/add-location")
  ResponseEntity<String> addLocation(@RequestParam String city, @RequestParam String country);

  @PostMapping("/add-locations")
  public ResponseEntity<String> addLocations(@RequestBody List<LocationRequest> locationRequests);

  @DeleteMapping("/delete-location")
  public ResponseEntity<String> deleteLocation(@RequestParam String city);
}
