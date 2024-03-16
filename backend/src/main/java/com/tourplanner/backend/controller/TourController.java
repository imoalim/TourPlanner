package com.tourplanner.backend.controller;

import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tours")
public class TourController {

    private final TourService tourService;

    @PostMapping("")
    public ResponseEntity<Void> createTour(@RequestBody TourDTO tourDTO) {
        tourService.createTour(tourDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("")
    public List<TourDTO> getAllTours() {
        return tourService.getAllTours();
    }
    @GetMapping("/{id}")
    public List<TourDTO> getTourById(@PathVariable Long id) {return tourService.getTourById(id);}
    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteTourById(@PathVariable Long id){tourService.deleteTourByID(id); return ResponseEntity.noContent().build();}

    @PutMapping("/update{id}")
    public ResponseEntity<TourDTO> updateTour(@PathVariable Long id, @RequestBody TourDTO tourDTO){
        TourDTO updateTour = tourService.updateTour(id, tourDTO); return ResponseEntity.ok(updateTour);}
}
