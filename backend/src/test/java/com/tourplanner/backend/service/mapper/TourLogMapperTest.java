package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TourLogMapperTest {

    private TourLogMapper tourLogMapper;

    @BeforeEach
    void setUp() {
        tourLogMapper = new TourLogMapper();
    }

    @Test
    public void shouldMapTourLogToTourLogDTO() {
        // Given
        Tour tour = Tour.builder()
                .id(1L)
                .name("Morning Bike Ride")
                .description("A leisurely bike ride through the city parks.")
                .fromLocation("Central Park")
                .toLocation("Riverside Park")
                .transportType("Bike")
                .distance(5.0)
                .estimatedTime(Duration.parse("PT1H"))
                .imageUrl("https://example.com/path/to/image")
                .build();

        TourLog tourLog = TourLog.builder()
                .id(1L)
                .dateTime(LocalDateTime.parse("2024-03-20T15:00:00"))
                .comment("Beautiful scenery and a pleasant climb.")
                .difficulty("Moderate")
                .distance(12.5)
                .totalTime(Duration.parse("PT5H30M"))
                .rating(4.5)
                .tour(tour)
                .build();

        // When
        TourLogDTO tourLogDTO = tourLogMapper.mapToDto(tourLog);

        // Then
        assertEquals(tourLogDTO.getId(), tourLog.getId());
        assertEquals(tourLogDTO.getDateTime(), tourLog.getDateTime());
        assertEquals(tourLogDTO.getComment(), tourLog.getComment());
        assertEquals(tourLogDTO.getDifficulty(), tourLog.getDifficulty());
        assertEquals(tourLogDTO.getDistance(), tourLog.getDistance());
        assertEquals(tourLogDTO.getTotalTime(), tourLog.getTotalTime());
        assertEquals(tourLogDTO.getRating(), tourLog.getRating());
        assertEquals(tourLogDTO.getTourId(), tourLog.getTour().getId());
    }
}