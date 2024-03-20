package com.tourplanner.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "TourLog")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDateTime dateTime;
    private String comment;
    private String difficulty;
    private Double distance;
    private Duration totalTime;
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
}
