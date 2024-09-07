package com.example.weatherService.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class LocationUrls {

  public static final Map<String, String> LOCATION_URLS = Map.of(
    "jastarnia", "https://api.weatherbit.io/v2.0/forecast/daily?city=Jastarnia&country=pl&key=%s",
    "bridgetown", "https://api.weatherbit.io/v2.0/forecast/daily?city=Bridgetown&country=Barbados&key=%s",
    "fortaleza", "https://api.weatherbit.io/v2.0/forecast/daily?city=Fortaleza&country=Brazil&key=%s",
    "pissouri", "https://api.weatherbit.io/v2.0/forecast/daily?city=Pissouri&country=Cyprus&key=%s",
    "le Morne", "https://api.weatherbit.io/v2.0/forecast/daily?city=Le+Morne&country=mq&key=%s"
  );
}
