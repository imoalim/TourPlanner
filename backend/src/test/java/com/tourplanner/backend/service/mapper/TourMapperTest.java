package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
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
        TourResponseDTO tourResponseDTO = tourMapper.mapToDto(tour);

        // Then
        assertEquals(tourResponseDTO.getId(), tour.getId());
        assertEquals(tourResponseDTO.getName(), tour.getName());
        assertEquals(tourResponseDTO.getDescription(), tour.getDescription());
        assertEquals(tourResponseDTO.getFromLocation(), tour.getFromLocation());
        assertEquals(tourResponseDTO.getToLocation(), tour.getToLocation());
        assertEquals(tourResponseDTO.getTransportType(), tour.getTransportType());
        assertEquals(tourResponseDTO.getDistance(), tour.getDistance());
        assertEquals(tourResponseDTO.getEstimatedTime(), tour.getEstimatedTime());
        assertEquals(tourResponseDTO.getImageUrl(), tour.getImageUrl());
    }
}