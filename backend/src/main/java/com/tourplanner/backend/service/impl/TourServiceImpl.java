package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.GenericService;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.dto.tour.TourRequestDTO;
import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
import com.tourplanner.backend.service.mapper.TourLogMapper;
import com.tourplanner.backend.service.mapper.TourMapper;
import com.tourplanner.backend.service.ors.ORSService;
import com.tourplanner.backend.service.ors.OrsParameters;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TourServiceImpl implements GenericService<TourRequestDTO, TourResponseDTO, Long> {

    private final TourRepository tourRepository;

    private final TourMapper tourMapper;

    private final TourLogMapper tourLogMapper;

    private final ORSService orsService;

    void checkIfTourExist(Long id){
        if (!tourRepository.existsById(id))
            throw new EntityNotFoundException("Tour not found for id " + id);
    }

    @Override
    public TourResponseDTO create(TourRequestDTO tourRequestDTO) {
        OrsParameters orsParameters;
        try {
            orsParameters = orsService.getOrsParameters(tourRequestDTO.getFromLocation(), tourRequestDTO.getToLocation(), tourRequestDTO.getTransportType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Tour tour = Tour.builder()
                .name(tourRequestDTO.getName())
                .description(tourRequestDTO.getDescription())
                .fromLocation(tourRequestDTO.getFromLocation())
                .toLocation(tourRequestDTO.getToLocation())
                .transportType(tourRequestDTO.getTransportType())
                .distance(orsParameters.distance())
                .estimatedTime(orsParameters.estimatedTime())
                .imageUrl(orsParameters.imageUrl())
                .popularity(Popularity.UNKNOWN)
                .childFriendliness(ChildFriendliness.UNKNOWN)
                .build();
        tourRepository.save(tour);
        return tourMapper.mapToDto(tour);
    }

    @Override
    public List<TourResponseDTO> findAll() {
        return tourMapper.mapToDto(tourRepository.findAll());
    }

    @Override
    public TourResponseDTO findById(Long id) {
        // Using Optional.map to convert the found Tour into a TourDTO, if present.
        // orElseThrow is used to throw an exception if the Tour is not found.
        checkIfTourExist(id);

        return tourRepository.findById(id)
                .map(tourMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + id));
    }

    @Override
    public void deleteById(Long id) {
        checkIfTourExist(id);
        tourRepository.deleteById(id);
    }

    @Override
    public TourResponseDTO update(Long id, TourRequestDTO tourRequestDTO) {
        checkIfTourExist(id);
        // Retrieve the existing tour from the database
        Tour existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + id));

        // Update the existing tour's fields with the values from the DTO
        existingTour.setName(tourRequestDTO.getName());
        existingTour.setDescription(tourRequestDTO.getDescription());
        existingTour.setFromLocation(tourRequestDTO.getFromLocation());
        existingTour.setToLocation(tourRequestDTO.getToLocation());
        existingTour.setTransportType(tourRequestDTO.getTransportType());

        OrsParameters orsParameters;
        try {
            orsParameters = orsService.getOrsParameters(tourRequestDTO.getFromLocation(), tourRequestDTO.getToLocation(), tourRequestDTO.getTransportType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        existingTour.setDistance(orsParameters.distance());
        existingTour.setEstimatedTime(orsParameters.estimatedTime());
        existingTour.setImageUrl(orsParameters.imageUrl());

        // Save the updated tour back to the database
        tourRepository.save(existingTour);

        return tourMapper.mapToDto(existingTour);
    }

    public List<TourLogDTO> getAllTourLogsForThisTour(Long tourId) {
        checkIfTourExist(tourId);
        Tour tour = tourRepository.findById(tourId).orElseThrow();
        List<TourLog> tourLogs = tour.getLogs();
        return tourLogMapper.mapToDto(tourLogs);
    }

    public void updateComputedTourAttributes(Long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + tourId));

        List<TourLog> tourLogs = tour.getLogs();

        tour.setPopularity(calculateTourPopularity(tourLogs));
        tour.setChildFriendliness(calculateTourChildFriendliness(tourLogs));

        tourRepository.save(tour);
    }

    private Popularity calculateTourPopularity(List<TourLog> tourLogs) {
        int amountOfTourLogs = tourLogs.size();

        if(amountOfTourLogs == 1 || amountOfTourLogs == 2) {
            return Popularity.LOW;
        } else if(amountOfTourLogs == 3 || amountOfTourLogs == 4) {
            return Popularity.MEDIUM;
        } else if(amountOfTourLogs >= 5) {
            return Popularity.HIGH;
        }

        return Popularity.UNKNOWN;
    }

    private ChildFriendliness calculateTourChildFriendliness(List<TourLog> tourLogs) {
        int amountOfTourLogs = tourLogs.size();
        int overallTourPoints = 0;

        for(TourLog tourLog : tourLogs) {
            switch(tourLog.getDifficulty()) {
                case EASY: overallTourPoints += 2; break;
                case MODERATE : overallTourPoints += 1; break;
                case HARD :
                default: break;
            }
            System.out.println("Overall tour points after difficulty check: " + overallTourPoints);

            double totalTourTime = tourLog.getTotalTime();
            if(totalTourTime <= 1800) {
                overallTourPoints += 2;
            } else if (overallTourPoints > 1800 && totalTourTime <= 3600) {
                overallTourPoints += 1;
            }

            System.out.println("Overall tour points after total time check: " + overallTourPoints);

            double totalTourDistance = tourLog.getDistance();
            if(totalTourDistance <= 3500) {
                overallTourPoints += 2;
            } else if (totalTourDistance > 3500 && totalTourDistance <= 7000) {
                overallTourPoints += 1;
            }
            System.out.println("Overall tour points after distance check: " + overallTourPoints);
        }

        double weightedTourPoints = (double) overallTourPoints / amountOfTourLogs;
        System.out.println("Weighted tour points: " + weightedTourPoints);

        if(weightedTourPoints >= 5) {
            return ChildFriendliness.HIGH;
        } else if (weightedTourPoints < 5 && overallTourPoints >= 3) {
            return ChildFriendliness.MEDIUM;
        } else if (weightedTourPoints < 3 && overallTourPoints >= 0) {
            return ChildFriendliness.LOW;
        }

        return ChildFriendliness.UNKNOWN;
    }
}
