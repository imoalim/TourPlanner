package com.tourplanner.backend.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericController<REQ, RES, ID> {
    ResponseEntity<RES> create(REQ dto);
    ResponseEntity<List<RES>> findAll();
    ResponseEntity<RES> findById(ID id);
    ResponseEntity<Void> deleteById(ID id);
    ResponseEntity<RES> update(ID id, REQ dto);
}
