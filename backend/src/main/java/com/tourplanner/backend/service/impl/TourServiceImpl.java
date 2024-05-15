package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.GenericService;
import com.tourplanner.backend.service.map.GeocodeRetriever;
import com.tourplanner.backend.service.mapper.TourMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TourServiceImpl implements GenericService<TourDTO, Long> {

    private final TourRepository tourRepository;

    private final TourMapper tourMapper;

    private final GeocodeRetriever geocodeRetriever;

    void checkIfTourExist(Long id){
        if (!tourRepository.existsById(id))
            throw new EntityNotFoundException("Tour not found for id " + id);
    }

    @Override
    public TourDTO create(TourDTO tourDTO) {
        Mono<String[]> geoData = geocodeRetriever.getCoordinates(tourDTO.getFromLocation());
        geoData.subscribe(System.out::println);
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
        return tourMapper.mapToDto(tour);
    }

    @Override
    public List<TourDTO> findAll() {
        return tourMapper.mapToDto(tourRepository.findAll());
    }

    @Override
    public List<TourDTO> findById(Long id) {
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
    public void deleteById(Long id) {
        checkIfTourExist(id);
        tourRepository.deleteById(id);
    }

    @Override
    public TourDTO update(Long id, TourDTO tourDTO) {
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
}
