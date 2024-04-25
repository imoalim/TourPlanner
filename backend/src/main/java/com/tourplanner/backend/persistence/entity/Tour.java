package com.tourplanner.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
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
    private Duration estimatedTime;
    private String imageUrl;
    //the image, the distance, and the time should be retrieved by a REST request using the OpenRouteservice.org APIs and OpenStreetMap Tile Server

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLog> logs;
}
