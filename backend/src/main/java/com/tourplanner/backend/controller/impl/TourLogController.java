package com.tourplanner.backend.controller.impl;

import com.tourplanner.backend.controller.IGenericController;
import com.tourplanner.backend.service.IGenericService;
import com.tourplanner.backend.service.dto.TourLogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tourLogs")
public class TourLogController implements IGenericController<TourLogDTO, Long> {

    private final IGenericService<TourLogDTO, Long> tourLogService;

    @Override
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TourLogDTO dto) {
        tourLogService.create(dto);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TourLogDTO>> findAll() {
        return ResponseEntity.ok(tourLogService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<List> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tourLogService.findById(id));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        tourLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TourLogDTO> update(@PathVariable Long id, @RequestBody TourLogDTO dto) {
        TourLogDTO updatedTour = tourLogService.update(id, dto);
        return ResponseEntity.ok(updatedTour);
    }
}
