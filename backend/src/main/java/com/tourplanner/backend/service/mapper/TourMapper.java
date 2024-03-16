package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.model.Tour;
import com.tourplanner.backend.service.dto.TourDTO;
import org.springframework.stereotype.Component;

@Component
public class TourMapper extends AbstractMapper<Tour, TourDTO> {
    @Override
    public TourDTO mapToDto(Tour sourceTour) {
        return TourDTO.builder()
                .id(sourceTour.getId())
                .name(sourceTour.getName())
                .description(sourceTour.getDescription())
                .fromLocation(sourceTour.getFromLocation())
                .toLocation(sourceTour.getToLocation())
                .transportType(sourceTour.getTransportType())
                .distance(sourceTour.getDistance())
                .estimatedTime(sourceTour.getEstimatedTime())
                .imageUrl(sourceTour.getImageUrl())
                .build();
    }
}
