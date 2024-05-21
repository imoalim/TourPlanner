package com.tourplanner.backend.controller.impl;

import com.tourplanner.backend.controller.GenericController;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.dto.tour.TourRequestDTO;
import com.tourplanner.backend.service.dto.tour.TourResponseDTO;
import com.tourplanner.backend.service.impl.TourServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tours")
public class TourController implements GenericController<TourRequestDTO, TourResponseDTO, Long> {

    private final TourServiceImpl tourService;

    @Override
    @PostMapping
    public ResponseEntity<TourResponseDTO> create(@RequestBody TourRequestDTO tourRequestDTO) {
        TourResponseDTO createdTour = tourService.create(tourRequestDTO);
        return ResponseEntity.ok(createdTour);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TourResponseDTO>> findAll() {
        return ResponseEntity.ok(tourService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TourResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.findById(id));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tourService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TourResponseDTO> update(@PathVariable Long id, @RequestBody TourRequestDTO tourRequestDTO) {
        TourResponseDTO updatedTour = tourService.update(id, tourRequestDTO);
        return ResponseEntity.ok(updatedTour);
    }

    @GetMapping("/{id}/tourLogs")
    public ResponseEntity<List<TourLogDTO>> findAllTourLogsForThisTour(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getAllTourLogsForThisTour(id));
    }
}
