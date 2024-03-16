package com.tourplanner.backend.service;

import com.tourplanner.backend.service.dto.TourDTO;

import java.util.List;

public interface TourService {
    void createTour(TourDTO tour);

    List<TourDTO> getAllTours();

    List<TourDTO> getTourById(Long id);

    void deleteTourByID(Long id);

    TourDTO updateTour(Long id, TourDTO tourDTO);
}
