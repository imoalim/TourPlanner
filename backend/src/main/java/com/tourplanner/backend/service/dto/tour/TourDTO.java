package com.tourplanner.backend.service.dto.tour;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import com.tourplanner.backend.service.dto.map.MapInfoDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourDTO {
    private Long id;
    @NotEmpty(message = "name cannot be empty")
    private String name;
    private String description;
    @NotEmpty(message = "fromLocation cannot be empty")
    private String fromLocation;
    @NotEmpty(message = "toLocation cannot be empty")
    private String toLocation;
    @NotEmpty(message = "transportType cannot be empty")
    private String transportType;
    private Double distance;
    private Double estimatedTime;
    private MapInfoDTO mapInfoDTO;
    private Popularity popularity;
    private ChildFriendliness childFriendliness;
}