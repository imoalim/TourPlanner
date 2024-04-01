package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.mapper.TourMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourServiceImplTest {

    @InjectMocks
    private TourServiceImpl tourService;

    // Dependencies
    @Mock
    private TourRepository tourRepository;
    @Mock
    private TourMapper tourMapper;

    private static Tour tour;
    private static TourDTO tourDTO;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        tour = Tour.builder()
                .name("Morning Bike Ride")
                .description("A leisurely bike ride through the city parks.")
                .fromLocation("Central Park")
                .toLocation("Riverside Park")
                .transportType("Bike")
                .distance(5.0)
                .estimatedTime(Duration.parse("PT1H"))
                .imageUrl("https://example.com/path/to/image")
                .build();

        tourDTO = TourDTO.builder()
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
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close the resources
        closeable.close();
    }

    @Test
    public void shouldSaveTourSuccessfully() {
        // Given
        // Mock the calls
        when(tourRepository.save(tour)).thenReturn(tour);
        when(tourMapper.mapToDto(tour)).thenReturn(tourDTO);

        // When
        TourDTO tourResponseDTO = tourService.create(tourDTO);

        // Then
        assertNotNull(tourResponseDTO.getId());
        assertEquals(tourResponseDTO.getId(), tourDTO.getId());
        assertEquals(tourResponseDTO.getName(), tourDTO.getName());
        assertEquals(tourResponseDTO.getDescription(), tourDTO.getDescription());
        assertEquals(tourResponseDTO.getFromLocation(), tourDTO.getFromLocation());
        assertEquals(tourResponseDTO.getToLocation(), tourDTO.getToLocation());
        assertEquals(tourResponseDTO.getTransportType(), tourDTO.getTransportType());
        assertEquals(tourResponseDTO.getDistance(), tourDTO.getDistance());
        assertEquals(tourResponseDTO.getEstimatedTime(), tourDTO.getEstimatedTime());
        assertEquals(tourResponseDTO.getImageUrl(), tourDTO.getImageUrl());

        verify(tourRepository, times(1)).save(tour);
        verify(tourMapper, times(1)).mapToDto(tour);
    }

    @Test
    public void shouldReturnAllTours() {
        // Given
        List<Tour> tours = new ArrayList<>();
        tours.add(tour);

        // Mock the calls
        when(tourRepository.findAll()).thenReturn(tours);
        when(tourMapper.mapToDto(tours)).thenReturn(Collections.singletonList(tourDTO));

        // When
        List<TourDTO> tourDTOs = tourService.findAll();

        // Then
        assertEquals(tours.size(), tourDTOs.size());
        verify(tourRepository, times(1)).findAll();
    }
}