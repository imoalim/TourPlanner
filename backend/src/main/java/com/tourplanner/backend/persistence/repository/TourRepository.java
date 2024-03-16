package com.tourplanner.backend.persistence.repository;

import com.tourplanner.backend.persistence.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
