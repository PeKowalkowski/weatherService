package com.example.weatherService.controllers;

import com.example.weatherService.models.Location;
import com.example.weatherService.responses.SurfingLocationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/surfing")
public interface SurfingLocationController {

  @GetMapping("/best-location")
  public ResponseEntity<SurfingLocationResponse> getBestSurfingLocation(@RequestBody List<Location> locations,
                                                                        @RequestParam String date);
}
