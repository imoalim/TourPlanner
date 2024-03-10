package com.tourplanner.backend.repository;

import com.tourplanner.backend.model.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {
}
