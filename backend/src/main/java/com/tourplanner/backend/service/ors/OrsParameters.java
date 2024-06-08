package com.tourplanner.backend.service.ors;

import com.tourplanner.backend.service.dto.map.MapInfoDTO;

public record OrsParameters(Double distance, Double estimatedTime, MapInfoDTO mapInfoDTO) {
}
