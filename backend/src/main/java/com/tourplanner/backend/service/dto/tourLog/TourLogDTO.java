package com.tourplanner.backend.service.dto.tourLog;

import com.tourplanner.backend.persistence.attributes.tourLog.Difficulty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourLogDTO {
    private Long id;
    private LocalDateTime dateTime;
    private String comment;
    private Difficulty difficulty;
    private Double distance;
    private Double totalTime;
    private Double rating;
    @NotNull(message = "tourId cannot be null")
    private Long tourId;
}