package com.tourplanner.backend.repository;

import com.tourplanner.backend.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
}
