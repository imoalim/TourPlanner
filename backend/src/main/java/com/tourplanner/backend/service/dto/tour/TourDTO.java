package com.tourplanner.backend.service.dto.tour;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
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