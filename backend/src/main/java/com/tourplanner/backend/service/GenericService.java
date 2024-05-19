package com.tourplanner.backend.service;

import java.util.List;

public interface GenericService<REQ, RES, ID> {
    RES create(REQ dto);
    List<RES> findAll();
    RES findById(ID id);
    void deleteById(ID id);
    RES update(ID id, REQ dto);
}
