package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.TourService;
import com.tourplanner.backend.service.mapper.TourMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    private final TourMapper tourMapper;

    @Override
    public void createTour(TourDTO tourDTO) {
        Tour tour = Tour.builder()
                .name(tourDTO.getName())
                .description(tourDTO.getDescription())
                .fromLocation(tourDTO.getFromLocation())
                .toLocation(tourDTO.getToLocation())
                .transportType(tourDTO.getTransportType())
                .distance(tourDTO.getDistance())
                .estimatedTime(tourDTO.getEstimatedTime())
                .imageUrl(tourDTO.getImageUrl())
                .build();
        tourRepository.save(tour);
    }

    @Override
    public List<TourDTO> getAllTours() {
        return tourMapper.mapToDto(tourRepository.findAll());
    }

    @Override
    public List<TourDTO> getTourById(Long id) {
        // Using Optional.map to convert the found Tour into a TourDTO, if present.
        // orElseThrow is used to throw an exception if the Tour is not found.
        checkIfTourExist(id);
        TourDTO tourDTO = tourRepository.findById(id)
                .map(tourMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + id));

        // Since the method expects a list, we wrap the single TourDTO in a list.
        return Collections.singletonList(tourDTO);
    }
    @Override
    public void deleteTourByID(Long id) {
        checkIfTourExist(id);
        tourRepository.deleteById(id);
    }

    @Override
    public TourDTO updateTour(Long id, TourDTO tourDTO) {
        checkIfTourExist(id);
        // Retrieve the existing tour from the database
        Tour existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + id));

        // Update the existing tour's fields with the values from the DTO
        existingTour.setName(tourDTO.getName());
        existingTour.setDescription(tourDTO.getDescription());
        existingTour.setFromLocation(tourDTO.getFromLocation());
        existingTour.setToLocation(tourDTO.getToLocation());
        existingTour.setTransportType(tourDTO.getTransportType());
        existingTour.setDistance(tourDTO.getDistance());
        existingTour.setEstimatedTime(tourDTO.getEstimatedTime());
        existingTour.setImageUrl(tourDTO.getImageUrl());

        // Save the updated tour back to the database
        tourRepository.save(existingTour);

        return tourMapper.mapToDto(existingTour);
    }
    boolean checkIfTourExist(Long id){
        if (!tourRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour not found for id " + id);
        }
        return true;
    }
}
