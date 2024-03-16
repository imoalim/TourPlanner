package com.tourplanner.backend.controller;

import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.interf.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TourController {

    private final TourService tourService;

    @PostMapping("/tours")
    public ResponseEntity<Void> createTour(@RequestBody TourDTO tourDTO) {
        tourService.createTour(tourDTO);
        return ResponseEntity.ok().build();
    }
}
