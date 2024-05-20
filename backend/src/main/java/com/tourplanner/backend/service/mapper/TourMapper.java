package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.service.dto.TourResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TourMapper extends AbstractMapper<Tour, TourResponseDTO> {
    @Override
    public TourResponseDTO mapToDto(Tour sourceTour) {
        return TourResponseDTO.builder()
                .id(sourceTour.getId())
                .name(sourceTour.getName())
                .description(sourceTour.getDescription())
                .fromLocation(sourceTour.getFromLocation())
                .toLocation(sourceTour.getToLocation())
                .transportType(sourceTour.getTransportType())
                .distance(sourceTour.getDistance())
                .estimatedTime(sourceTour.getEstimatedTime())
                .imageUrl(sourceTour.getImageUrl())
                .popularity(sourceTour.getPopularity())
                .childFriendliness(sourceTour.getChildFriendliness())
                .build();
    }
}
