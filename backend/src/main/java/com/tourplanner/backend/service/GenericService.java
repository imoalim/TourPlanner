package com.tourplanner.backend.service;

import java.util.List;

public interface GenericService<DTO, ID> {
    DTO create(DTO dto);
    List<DTO> findAll();
    DTO findById(ID id);
    void deleteById(ID id);
    DTO update(ID id, DTO dto);
}
