package com.tourplanner.backend.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericController<T, ID> {
    ResponseEntity<Void> create(T dto);
    ResponseEntity<List<T>> findAll();
    ResponseEntity<List<T>> findById(ID id);
    ResponseEntity<Void> deleteById(ID id);
    ResponseEntity<T> update(ID id, T dto);
}
