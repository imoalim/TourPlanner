//package com.tourplanner.backend.service.impl;
//
//import com.tourplanner.backend.persistence.entity.Tour;
//import com.tourplanner.backend.persistence.repository.TourRepository;
//import com.tourplanner.backend.service.dto.tour.TourRequestDTO;
//import com.tourplanner.backend.service.mapper.TourMapper;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TourServiceImplTest {
//
//    @InjectMocks
//    private TourServiceImpl tourService;
//
//    // Dependencies
//    @Mock
//    private TourRepository tourRepository;
//    @Mock
//    private TourMapper tourMapper;
//
//    private static Tour tour;
//    private static TourRequestDTO tourRequestDTO;
//
//    private AutoCloseable closeable;
//
//    @BeforeEach
//    void setUp() {
//        closeable = MockitoAnnotations.openMocks(this);
//
//        tour = Tour.builder()
//                .name("Morning Bike Ride")
//                .description("A leisurely bike ride through the city parks.")
//                .fromLocation("Central Park")
//                .toLocation("Riverside Park")
//                .transportType("Bike")
//                .distance(5.0)
//                .estimatedTime(Duration.parse("PT1H"))
//                .imageUrl("https://example.com/path/to/image")
//                .build();
//
//        tourRequestDTO = TourRequestDTO.builder()
//                .id(1L)
//                .name("Morning Bike Ride")
//                .description("A leisurely bike ride through the city parks.")
//                .fromLocation("Central Park")
//                .toLocation("Riverside Park")
//                .transportType("Bike")
//                .distance(5.0)
//                .estimatedTime(Duration.parse("PT1H"))
//                .imageUrl("https://example.com/path/to/image")
//                .build();
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        // Close the resources
//        closeable.close();
//    }
//
//    @Test
//    public void shouldSaveTourSuccessfully() {
//        // Given
//        // Mock the calls
//        when(tourRepository.save(tour)).thenReturn(tour);
//        when(tourMapper.mapToDto(tour)).thenReturn(tourRequestDTO);
//
//        // When
//        TourRequestDTO tourResponseDTO = tourService.create(tourRequestDTO);
//
//        // Then
//        assertNotNull(tourResponseDTO.getId());
//        assertEquals(tourResponseDTO.getId(), tourRequestDTO.getId());
//        assertEquals(tourResponseDTO.getName(), tourRequestDTO.getName());
//        assertEquals(tourResponseDTO.getDescription(), tourRequestDTO.getDescription());
//        assertEquals(tourResponseDTO.getFromLocation(), tourRequestDTO.getFromLocation());
//        assertEquals(tourResponseDTO.getToLocation(), tourRequestDTO.getToLocation());
//        assertEquals(tourResponseDTO.getTransportType(), tourRequestDTO.getTransportType());
//        assertEquals(tourResponseDTO.getDistance(), tourRequestDTO.getDistance());
//        assertEquals(tourResponseDTO.getEstimatedTime(), tourRequestDTO.getEstimatedTime());
//        assertEquals(tourResponseDTO.getImageUrl(), tourRequestDTO.getImageUrl());
//
//        verify(tourRepository, times(1)).save(tour);
//        verify(tourMapper, times(1)).mapToDto(tour);
//    }
//
//    @Test
//    public void shouldReturnAllTours() {
//        // Given
//        List<Tour> tours = new ArrayList<>();
//        tours.add(tour);
//
//        // Mock the calls
//        when(tourRepository.findAll()).thenReturn(tours);
//        when(tourMapper.mapToDto(tours)).thenReturn(Collections.singletonList(tourRequestDTO));
//
//        // When
//        List<TourRequestDTO> tourRequestDTOS = tourService.findAll();
//
//        // Then
//        assertEquals(tours.size(), tourRequestDTOS.size());
//        verify(tourRepository, times(1)).findAll();
//    }
//}