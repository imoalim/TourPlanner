package com.tourplanner.backend.persistence.entity;

import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "map_info_id")
    @ToString.Exclude
    private MapInfo mapInfo;

    @Enumerated(EnumType.STRING)
    private Popularity popularity;

    @Enumerated(EnumType.STRING)
    private ChildFriendliness childFriendliness;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TourLog> logs = new ArrayList<>();
}
