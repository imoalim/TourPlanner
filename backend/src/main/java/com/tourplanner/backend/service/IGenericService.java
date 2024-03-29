package com.tourplanner.backend.service;

import java.util.List;

public interface IGenericService <T, ID> {
    T create(T dto);
    List<T> findAll();
    List<T> findById(ID id);
    void deleteById(ID id);
    T update(ID id, T dto);
}
