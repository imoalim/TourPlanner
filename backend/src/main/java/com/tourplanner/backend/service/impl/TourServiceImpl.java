package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.model.Tour;
import com.tourplanner.backend.repository.TourRepository;
import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.interf.TourService;
import com.tourplanner.backend.service.mapper.TourMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
