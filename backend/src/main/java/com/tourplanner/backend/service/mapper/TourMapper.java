package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.MapInfo;
import com.tourplanner.backend.service.dto.map.MapInfoDTO;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import org.springframework.stereotype.Component;

@Component
public class TourMapper extends AbstractMapper<Tour, TourDTO> {
    @Override
    public TourDTO mapToDto(Tour sourceTour) {
        MapInfo mapInfo = sourceTour.getMapInfo();
        MapInfoDTO mapInfoDTO = mapInfo != null ? MapInfoDTO.builder()
                .startLat(mapInfo.getStartLat())
                .startLng(mapInfo.getStartLng())
                .endLat(mapInfo.getEndLat())
                .endLng(mapInfo.getEndLng())
                .centerLat(mapInfo.getCenterLat())
                .centerLng(mapInfo.getCenterLng())
                .route(mapInfo.getRoute())
                .build() : null;

        return TourDTO.builder()
                .id(sourceTour.getId())
                .name(sourceTour.getName())
                .description(sourceTour.getDescription())
                .fromLocation(sourceTour.getFromLocation())
                .toLocation(sourceTour.getToLocation())
                .transportType(sourceTour.getTransportType())
                .distance(sourceTour.getDistance())
                .estimatedTime(sourceTour.getEstimatedTime())
                .mapInfoDTO(mapInfoDTO)
                .popularity(sourceTour.getPopularity())
                .childFriendliness(sourceTour.getChildFriendliness())
                .build();
    }
}
