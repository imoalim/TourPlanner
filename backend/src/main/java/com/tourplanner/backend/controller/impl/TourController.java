package com.tourplanner.backend.controller.impl;

import com.tourplanner.backend.controller.IGenericController;
import com.tourplanner.backend.service.dto.TourDTO;
import com.tourplanner.backend.service.IGenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tour")
public class TourController implements IGenericController<TourDTO, Long> {


    private final IGenericService<TourDTO, Long> tourService;

    @Override
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TourDTO tourDTO) {
        tourService.create(tourDTO);
        return ResponseEntity.ok().build();
    }
    @Override
    @GetMapping
    public ResponseEntity<List<TourDTO>> findAll() {
        return ResponseEntity.ok(tourService.findAll());
    }

    @Override
    @GetMapping("/find{id}")
    public ResponseEntity<List> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.findById(id));
    }

    @Override
    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/update{id}")
    public ResponseEntity<TourDTO> update(@PathVariable Long id, @RequestBody TourDTO tourDTO) {
        TourDTO updatedTour = tourService.update(id, tourDTO);
        return ResponseEntity.ok(updatedTour);
    }
}
