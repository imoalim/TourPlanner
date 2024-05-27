package com.tourplanner.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MapInfo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Double startLat;
    private Double startLng;
    private Double endLat;
    private Double endLng;
    private Double centerLat;
    private Double centerLng;

    @Column(columnDefinition = "TEXT")
    private String route;

    @OneToOne(mappedBy = "mapInfo")
    private Tour tour;
}
