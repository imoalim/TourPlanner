package com.tourplanner.backend.persistence.entity;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tour")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
    private Double distance;
    private Double estimatedTime;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Popularity popularity;

    @Enumerated(EnumType.STRING)
    private ChildFriendliness childFriendliness;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLog> logs = new ArrayList<>();
}
