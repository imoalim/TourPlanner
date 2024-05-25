package com.tourplanner.backend.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericController<DTO, ID> {
    ResponseEntity<DTO> create(DTO dto);
    ResponseEntity<List<DTO>> findAll();
    ResponseEntity<DTO> findById(ID id);
    ResponseEntity<Void> deleteById(ID id);
    ResponseEntity<DTO> update(ID id, DTO dto);
}
