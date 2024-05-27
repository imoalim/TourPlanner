package com.tourplanner.backend.service.mapper;

import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import org.springframework.stereotype.Component;

@Component
public class TourLogMapper extends AbstractMapper<TourLog, TourLogDTO> {
    @Override
    public TourLogDTO mapToDto(TourLog sourceTourLog) {
        return TourLogDTO.builder()
                .id(sourceTourLog.getId())
                .dateTime(sourceTourLog.getDateTime())
                .comment(sourceTourLog.getComment())
                .difficulty(sourceTourLog.getDifficulty())
                .distance(sourceTourLog.getDistance())
                .totalTime(sourceTourLog.getTotalTime())
                .rating(sourceTourLog.getRating())
                .tourId(sourceTourLog.getTour().getId())
                .build();
    }
}
