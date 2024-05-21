package com.tourplanner.backend.service.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourRequestDTO {
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
}