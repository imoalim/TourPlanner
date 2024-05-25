package com.tourplanner.backend.controller.impl;

import com.tourplanner.backend.controller.GenericController;
import com.tourplanner.backend.service.GenericService;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tourLogs")
public class TourLogController implements GenericController<TourLogDTO, Long> {

    private final GenericService<TourLogDTO, Long> tourLogService;

    @Override
    @PostMapping
    public ResponseEntity<TourLogDTO> create(@RequestBody TourLogDTO tourLogDTO) {
        tourLogService.create(tourLogDTO);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TourLogDTO>> findAll() {
        return ResponseEntity.ok(tourLogService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TourLogDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tourLogService.findById(id));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tourLogService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TourLogDTO> update(@PathVariable Long id, @RequestBody TourLogDTO dto) {
        TourLogDTO updatedTour = tourLogService.update(id, dto);
        return ResponseEntity.ok(updatedTour);
    }
}
