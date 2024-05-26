package com.tourplanner.backend.service.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryDTO {
    private String tourName;
    private double averageDistance; // in km
    private double averageTime;     // in min
    private double averageRating;
}
