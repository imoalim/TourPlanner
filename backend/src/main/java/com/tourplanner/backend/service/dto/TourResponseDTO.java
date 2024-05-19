package com.tourplanner.backend.service.dto;

import com.tourplanner.backend.persistence.attributes.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.Popularity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
    private Double distance;
    private Double estimatedTime;
    private String imageUrl;
    private Popularity popularity;
    private ChildFriendliness childFriendliness;
}