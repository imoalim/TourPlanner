package com.tourplanner.backend.persistence.entity;

import com.tourplanner.backend.persistence.attributes.tourLog.Difficulty;
import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Double distance;
    private Double totalTime;
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    @ToString.Exclude
    private Tour tour;
}
