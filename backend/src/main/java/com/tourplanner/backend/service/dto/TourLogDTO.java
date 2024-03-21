package com.tourplanner.backend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourLogDTO {
    private Long id;
    private LocalDateTime dateTime;
    private String comment;
    private String difficulty;
    private Double distance;
    private Duration totalTime;
    private Double rating;
    private Long tourId;
}