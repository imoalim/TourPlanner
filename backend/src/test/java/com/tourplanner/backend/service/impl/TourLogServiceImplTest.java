package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourLogRepository;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.exception.ResourceNotFoundException;
import com.tourplanner.backend.service.mapper.TourLogMapper;
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

class TourLogServiceImplTest {

    @Mock
    private TourLogRepository tourLogRepository;

    @Mock
    private TourLogMapper tourLogMapper;

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourServiceImpl tourServiceImpl;

    @InjectMocks
    private TourLogServiceImpl tourLogServiceImpl;

    private TourLog tourLog;
    private TourLogDTO tourLogDTO;
    private Tour tour;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tour = Tour.builder()
                .id(1L)
                .name("Test Tour")
                .build();
        tourLog = TourLog.builder()
                .id(1L)
                .tour(tour)
                .comment("Test Comment")
                .build();
        tourLogDTO = TourLogDTO.builder()
                .id(1L)
                .tourId(1L)
                .comment("Test Comment")
                .build();
    }

    @Test
    void createTourLog() {
        when(tourRepository.findById(tourLogDTO.getTourId())).thenReturn(Optional.of(tour));
        when(tourLogRepository.save(any(TourLog.class))).thenReturn(tourLog);
        when(tourLogMapper.mapToDto(any(TourLog.class))).thenReturn(tourLogDTO);

        TourLogDTO createdTourLog = tourLogServiceImpl.create(tourLogDTO);

        assertEquals(tourLogDTO.getComment(), createdTourLog.getComment());
        verify(tourLogRepository, times(1)).save(any(TourLog.class));
        verify(tourServiceImpl, times(1)).updateComputedTourAttributes(tour.getId());
    }

    @Test
    void findAllTourLogs() {
        List<TourLog> tourLogs = List.of(tourLog);
        when(tourLogRepository.findAll()).thenReturn(tourLogs);
        when(tourLogMapper.mapToDto(anyList())).thenReturn(List.of(tourLogDTO));

        List<TourLogDTO> foundTourLogs = tourLogServiceImpl.findAll();

        assertEquals(1, foundTourLogs.size());
        verify(tourLogRepository, times(1)).findAll();
    }

    @Test
    void findTourLogById() {
        when(tourLogRepository.findById(1L)).thenReturn(Optional.of(tourLog));
        when(tourLogMapper.mapToDto(any(TourLog.class))).thenReturn(tourLogDTO);

        TourLogDTO foundTourLog = tourLogServiceImpl.findById(1L);

        assertEquals(tourLogDTO.getComment(), foundTourLog.getComment());
        verify(tourLogRepository, times(1)).findById(1L);
    }

    @Test
    void findTourLogByIdNotFound() {
        when(tourLogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourLogServiceImpl.findById(1L));
        verify(tourLogRepository, times(1)).findById(1L);
    }

    @Test
    void updateTourLog() {
        when(tourLogRepository.findById(1L)).thenReturn(Optional.of(tourLog));
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(tourLogRepository.save(any(TourLog.class))).thenReturn(tourLog);
        when(tourLogMapper.mapToDto(any(TourLog.class))).thenReturn(tourLogDTO);

        TourLogDTO updatedTourLog = tourLogServiceImpl.update(1L, tourLogDTO);

        assertEquals(tourLogDTO.getComment(), updatedTourLog.getComment());
        verify(tourLogRepository, times(1)).findById(1L);
        verify(tourLogRepository, times(1)).save(any(TourLog.class));
        verify(tourServiceImpl, times(1)).updateComputedTourAttributes(tour.getId());
    }

    @Test
    void deleteTourLog() {
        when(tourLogRepository.existsById(1L)).thenReturn(true);
        when(tourLogRepository.findById(1L)).thenReturn(Optional.of(tourLog));

        tourLogServiceImpl.deleteById(1L);

        verify(tourLogRepository, times(1)).deleteById(1L);
        verify(tourServiceImpl, times(1)).updateComputedTourAttributes(tour.getId());
    }

    @Test
    void deleteTourLogNotFound() {
        when(tourLogRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tourLogServiceImpl.deleteById(1L));
        verify(tourLogRepository, times(1)).existsById(1L);
    }
}