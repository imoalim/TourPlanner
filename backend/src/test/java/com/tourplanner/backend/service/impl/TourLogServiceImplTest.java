package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourLogRepository;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.tour.TourRequestDTO;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.mapper.TourLogMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourLogServiceImplTest {

    @InjectMocks
    private TourLogServiceImpl tourLogService;

    // Dependencies
    @Mock
    private TourRepository tourRepository;
    @Mock
    private TourLogRepository tourLogRepository;
    @Mock
    private TourLogMapper tourLogMapper;

    private static Tour tour;
    private static TourRequestDTO tourRequestDTO;
    private static TourLog tourLog;
    private static TourLogDTO tourLogDTO;

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

        tourRequestDTO = TourRequestDTO.builder()
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

        tourLog = TourLog.builder()
                .dateTime(LocalDateTime.parse("2024-03-20T15:00:00"))
                .comment("Beautiful scenery and a pleasant climb.")
                .difficulty("Moderate")
                .distance(12.5)
                .totalTime(Duration.parse("PT5H30M"))
                .rating(4.5)
                .tour(tour)
                .build();

        tourLogDTO = TourLogDTO.builder()
                .dateTime(LocalDateTime.parse("2024-03-20T15:00:00"))
                .comment("Beautiful scenery and a pleasant climb.")
                .difficulty("Moderate")
                .distance(12.5)
                .totalTime(Duration.parse("PT5H30M"))
                .rating(4.5)
                .tourId(tour.getId())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Close the resources
        closeable.close();
    }

    @Test
    void shouldSaveTourLogSuccessfully() {
        // Given
        // Mock the calls
        when(tourRepository.findById(tourLogDTO.getTourId())).thenReturn(Optional.of(tour));
        when(tourLogRepository.save(any(TourLog.class))).thenReturn(tourLog);
        when(tourLogMapper.mapToDto(tourLog)).thenReturn(tourLogDTO);

        // When
        TourLogDTO result = tourLogService.create(tourLogDTO);

        // Then
        assertNotNull(result);
        assertEquals(tourLogDTO, result);
        verify(tourRepository).findById(tourLogDTO.getTourId());
        verify(tourLogRepository).save(any(TourLog.class));
    }

    @Test
    void shouldThrowExceptionWhenTourNotFound() {
        // Given
        when(tourRepository.findById(tourLogDTO.getTourId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> tourLogService.create(tourLogDTO));
        verify(tourRepository).findById(tourLogDTO.getTourId());
    }

    @Test
    void shouldReturnAllTourLogs() {
        // Given
        List<TourLog> tourLogs = new ArrayList<>();
        tourLogs.add(tourLog);
        List<TourLogDTO> expectedTourLogDTOs = new ArrayList<>();
        expectedTourLogDTOs.add(tourLogDTO);

        when(tourLogRepository.findAll()).thenReturn(tourLogs);
        when(tourLogMapper.mapToDto(tourLogs)).thenReturn(expectedTourLogDTOs);

        // When
        List<TourLogDTO> expectedResult = tourLogService.findAll();

        // Then
        assertNotNull(expectedResult);
        assertEquals(expectedTourLogDTOs.size(), expectedResult.size());
        assertEquals(expectedTourLogDTOs, expectedResult);
        verify(tourLogRepository).findAll();
    }
}