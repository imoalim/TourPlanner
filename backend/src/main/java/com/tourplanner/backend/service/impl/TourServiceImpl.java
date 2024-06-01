package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import com.tourplanner.backend.persistence.entity.MapInfo;
import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.GenericService;
import com.tourplanner.backend.service.dto.map.MapInfoDTO;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.mapper.TourLogMapper;
import com.tourplanner.backend.service.mapper.TourMapper;
import com.tourplanner.backend.service.ors.ORSService;
import com.tourplanner.backend.service.ors.OrsParameters;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TourServiceImpl implements GenericService<TourDTO, Long> {

    private final TourRepository tourRepository;

    private final TourMapper tourMapper;

    private final TourLogMapper tourLogMapper;

    private final ORSService orsService;

    void checkIfTourExist(Long id){
        if (!tourRepository.existsById(id))
            throw new EntityNotFoundException("Tour not found for id " + id);
    }

    @Override
    public TourDTO create(TourDTO tourRequestDTO) {
        OrsParameters orsParameters = orsService.getOrsParameters(tourRequestDTO.getFromLocation(), tourRequestDTO.getToLocation(), tourRequestDTO.getTransportType());
        MapInfoDTO mapInfoDTO = orsParameters.mapInfoDTO();

        MapInfo mapInfo = MapInfo.builder()
                .startLat(mapInfoDTO.getStartLat())
                .startLng(mapInfoDTO.getStartLng())
                .endLat(mapInfoDTO.getEndLat())
                .endLng(mapInfoDTO.getEndLng())
                .centerLat(mapInfoDTO.getCenterLat())
                .centerLng(mapInfoDTO.getCenterLng())
                .route(mapInfoDTO.getRoute())
                .build();

        Tour tour = Tour.builder()
                .name(tourRequestDTO.getName())
                .description(tourRequestDTO.getDescription())
                .fromLocation(tourRequestDTO.getFromLocation())
                .toLocation(tourRequestDTO.getToLocation())
                .transportType(tourRequestDTO.getTransportType())
                .distance(orsParameters.distance())
                .estimatedTime(orsParameters.estimatedTime())
                .mapInfo(mapInfo)
                .popularity(Popularity.UNKNOWN)
                .childFriendliness(ChildFriendliness.UNKNOWN)
                .build();
        tourRepository.save(tour);
        return tourMapper.mapToDto(tour);
    }

    @Override
    public List<TourDTO> findAll() {
        return tourMapper.mapToDto(tourRepository.findAll());
    }

    @Override
    public TourDTO findById(Long id) {
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
    public TourDTO update(Long id, TourDTO tourRequestDTO) {
        // Retrieve the existing tour from the database
        Tour existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + id));

        // Update the existing tour's fields with the values from the DTO
        existingTour.setName(tourRequestDTO.getName());
        existingTour.setDescription(tourRequestDTO.getDescription());
        existingTour.setFromLocation(tourRequestDTO.getFromLocation());
        existingTour.setToLocation(tourRequestDTO.getToLocation());
        existingTour.setTransportType(tourRequestDTO.getTransportType());

        OrsParameters orsParameters = orsService.getOrsParameters(tourRequestDTO.getFromLocation(), tourRequestDTO.getToLocation(), tourRequestDTO.getTransportType());

        existingTour.setDistance(orsParameters.distance());
        existingTour.setEstimatedTime(orsParameters.estimatedTime());

        MapInfo mapInfo = existingTour.getMapInfo();
        if (mapInfo == null) {
            mapInfo = new MapInfo();
            existingTour.setMapInfo(mapInfo);
        }
        mapInfo.setStartLat(orsParameters.mapInfoDTO().getStartLat());
        mapInfo.setStartLng(orsParameters.mapInfoDTO().getStartLng());
        mapInfo.setEndLat(orsParameters.mapInfoDTO().getEndLat());
        mapInfo.setEndLng(orsParameters.mapInfoDTO().getEndLng());
        mapInfo.setCenterLat(orsParameters.mapInfoDTO().getCenterLat());
        mapInfo.setCenterLng(orsParameters.mapInfoDTO().getCenterLng());
        mapInfo.setRoute(orsParameters.mapInfoDTO().getRoute());

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
        int overallTourPoints = getOverallTourPoints(tourLogs);
        int amountOfTourLogs = tourLogs.size();

        double weightedTourPoints = (double) overallTourPoints / amountOfTourLogs;

        if(weightedTourPoints >= 5) {
            return ChildFriendliness.HIGH;
        } else if (weightedTourPoints < 5 && overallTourPoints >= 3) {
            return ChildFriendliness.MEDIUM;
        } else if (weightedTourPoints < 3 && overallTourPoints >= 0) {
            return ChildFriendliness.LOW;
        }

        return ChildFriendliness.UNKNOWN;
    }

    private int getOverallTourPoints(List<TourLog> tourLogs) {
        int overallTourPoints = 0;

        for(TourLog tourLog : tourLogs) {
            switch(tourLog.getDifficulty()) {
                case EASY: overallTourPoints += 2; break;
                case MODERATE : overallTourPoints += 1; break;
                case HARD :
                default: break;
            }

            double totalTourTime = tourLog.getTotalTime();
            if(totalTourTime <= 1800) {
                overallTourPoints += 2;
            } else if (totalTourTime > 1800 && totalTourTime <= 3600) {
                overallTourPoints += 1;
            }

            double totalTourDistance = tourLog.getDistance();
            if(totalTourDistance <= 3500) {
                overallTourPoints += 2;
            } else if (totalTourDistance > 3500 && totalTourDistance <= 7000) {
                overallTourPoints += 1;
            }
        }
        return overallTourPoints;
    }
}