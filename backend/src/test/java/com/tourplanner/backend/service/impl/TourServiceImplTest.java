package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import com.tourplanner.backend.persistence.attributes.tourLog.Difficulty;
import com.tourplanner.backend.persistence.entity.MapInfo;
import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.map.MapInfoDTO;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import com.tourplanner.backend.service.exception.ResourceNotFoundException;
import com.tourplanner.backend.service.mapper.TourMapper;
import com.tourplanner.backend.service.ors.ORSService;
import com.tourplanner.backend.service.ors.OrsParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TourServiceImplTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourMapper tourMapper;

    @Mock
    private ORSService orsService;

    @InjectMocks
    private TourServiceImpl tourServiceImpl;

    private Tour tour;
    private TourDTO tourDTO;
    private OrsParameters orsParameters;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MapInfoDTO mapInfoDTO = MapInfoDTO.builder()
                .startLat(48.194178)
                .startLng(16.411627)
                .endLat(48.193427)
                .endLng(16.411937)
                .centerLat(48.193802500000004)
                .centerLng(16.411711)
                .route("[[48.194178, 16.411627], [48.194056, 16.411811], [48.194028, 16.411853], " +
                        "[48.193799, 16.411485], [48.193723, 16.411487], [48.193427, 16.411937]]")
                .build();
        orsParameters = new OrsParameters(10.0, 30.0, mapInfoDTO);
        tour = Tour.builder()
                .id(1L)
                .name("Lazy walk")
                .fromLocation("Austria, 1030 Wien, Schnirchgasse 13")
                .toLocation("Austria, 1030 Wien, Schnirchgasse 11")
                .transportType("foot-walking")
                .distance(116.5)
                .estimatedTime(83.9)
                .mapInfo(MapInfo.builder().build())
                .popularity(Popularity.UNKNOWN)
                .childFriendliness(ChildFriendliness.UNKNOWN)
                .build();
        tourDTO = TourDTO.builder()
                .id(1L)
                .name("Test Tour")
                .fromLocation("Austria, 1030 Wien, Schnirchgasse 13")
                .toLocation("Austria, 1030 Wien, Schnirchgasse 11")
                .transportType("foot-walking")
                .distance(116.5)
                .estimatedTime(83.9)
                .mapInfoDTO(mapInfoDTO)
                .build();
    }

    @Test
    void createTour() {
        when(orsService.getOrsParameters(anyString(), anyString(), anyString())).thenReturn(orsParameters);
        when(tourRepository.save(any(Tour.class))).thenReturn(tour);
        when(tourMapper.mapToDto(any(Tour.class))).thenReturn(tourDTO);

        TourDTO createdTour = tourServiceImpl.create(tourDTO);

        assertEquals(tourDTO.getName(), createdTour.getName());
        verify(tourRepository, times(1)).save(any(Tour.class));
    }

    @Test
    void findAllTours() {
        List<Tour> tours = List.of(tour);
        when(tourRepository.findAll()).thenReturn(tours);
        when(tourMapper.mapToDto(anyList())).thenReturn(List.of(tourDTO));

        List<TourDTO> foundTours = tourServiceImpl.findAll();

        assertEquals(1, foundTours.size());
        verify(tourRepository, times(1)).findAll();
    }

    @Test
    void findTourById() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(tourMapper.mapToDto(any(Tour.class))).thenReturn(tourDTO);

        TourDTO foundTour = tourServiceImpl.findById(1L);

        assertEquals(tourDTO.getName(), foundTour.getName());
        verify(tourRepository, times(1)).findById(1L);
    }

    @Test
    void findTourByIdNotFound() {
        when(tourRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourServiceImpl.findById(1L));
        verify(tourRepository, times(1)).findById(1L);
    }

    @Test
    void updateTour() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(orsService.getOrsParameters(anyString(), anyString(), anyString())).thenReturn(orsParameters);
        when(tourRepository.save(any(Tour.class))).thenReturn(tour);
        when(tourMapper.mapToDto(any(Tour.class))).thenReturn(tourDTO);

        TourDTO updatedTour = tourServiceImpl.update(1L, tourDTO);

        assertEquals(tourDTO.getName(), updatedTour.getName());
        verify(tourRepository, times(1)).findById(1L);
        verify(tourRepository, times(1)).save(any(Tour.class));
    }

    @Test
    void deleteTour() {
        when(tourRepository.existsById(1L)).thenReturn(true);

        tourServiceImpl.deleteById(1L);

        verify(tourRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTourNotFound() {
        when(tourRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tourServiceImpl.deleteById(1L));
        verify(tourRepository, times(1)).existsById(1L);
    }

    @Test
    void updateComputedTourAttributes() {
        TourLog tourLog = TourLog.builder()
                .difficulty(Difficulty.EASY)
                .totalTime(1200.00)
                .distance(3000.00)
                .build();
        List<TourLog> tourLogs = List.of(tourLog);

        tour.setLogs(tourLogs);
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));

        tourServiceImpl.updateComputedTourAttributes(1L);

        assertEquals(Popularity.LOW, tour.getPopularity());
        assertEquals(ChildFriendliness.HIGH, tour.getChildFriendliness());
        verify(tourRepository, times(1)).save(tour);
    }
}