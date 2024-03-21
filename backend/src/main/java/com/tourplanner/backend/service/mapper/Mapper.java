package com.tourplanner.backend.service.mapper;

public interface Mapper<S, T> {
    T mapToDto(S source);
}
