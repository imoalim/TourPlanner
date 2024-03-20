package com.tourplanner.backend.persistence.repository;

import com.tourplanner.backend.persistence.entity.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {
}
