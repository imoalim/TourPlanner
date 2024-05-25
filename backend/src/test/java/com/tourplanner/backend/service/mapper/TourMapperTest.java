package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TourMapperTest {

    private TourMapper tourMapper;

    @BeforeEach
    void setUp() {
        tourMapper = new TourMapper();
    }

    @Test
    public void shouldMapTourToTourDTO() {
        // Given
        Tour tour = Tour.builder()
                .id(1L)
                .name("Morning Bike Ride")
                .description("A leisurely bike ride through the city parks.")
                .fromLocation("Central Park")
                .toLocation("Riverside Park")
                .transportType("Bike")
                .distance(5.0)
                .estimatedTime(3600.00)
                .imageUrl("https://example.com/path/to/image")
                .build();

        // When
        TourDTO tourDTO = tourMapper.mapToDto(tour);

        // Then
        assertEquals(tourDTO.getId(), tour.getId());
        assertEquals(tourDTO.getName(), tour.getName());
        assertEquals(tourDTO.getDescription(), tour.getDescription());
        assertEquals(tourDTO.getFromLocation(), tour.getFromLocation());
        assertEquals(tourDTO.getToLocation(), tour.getToLocation());
        assertEquals(tourDTO.getTransportType(), tour.getTransportType());
        assertEquals(tourDTO.getDistance(), tour.getDistance());
        assertEquals(tourDTO.getEstimatedTime(), tour.getEstimatedTime());
        assertEquals(tourDTO.getImageUrl(), tour.getImageUrl());
    }
}