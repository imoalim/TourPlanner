package com.tourplanner.backend.service.dto.map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapInfoDTO {
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private double centerLat;
    private double centerLng;
    private String route;
}
