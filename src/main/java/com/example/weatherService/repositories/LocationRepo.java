package com.example.weatherService.repositories;


import com.example.weatherService.entities.LocationEntity;
import com.example.weatherService.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepo extends JpaRepository<LocationEntity, Long> {
  Optional<LocationEntity> findByNameIgnoreCase(String name);
}
