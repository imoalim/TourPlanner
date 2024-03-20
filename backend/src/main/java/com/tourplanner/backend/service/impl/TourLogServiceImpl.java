package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourLogRepository;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.IGenericService;
import com.tourplanner.backend.service.dto.TourLogDTO;
import com.tourplanner.backend.service.mapper.TourLogMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@RequiredArgsConstructor
@Service
public class TourLogServiceImpl implements IGenericService<TourLogDTO, Long> {

    private final TourLogRepository tourLogRepository;

    private final TourRepository tourRepository;

    private final TourLogMapper tourLogMapper;
    void checkIfTourLogExist(Long id){
        if (!tourLogRepository.existsById(id)) {
            throw new EntityNotFoundException("TourLog not found for id " + id);
        }
    }

    @Override
    public TourLogDTO create(TourLogDTO dto) {
        Tour tour = tourRepository.findById(dto.getTourId())
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + dto.getTourId()));

        TourLog tourLog = TourLog.builder()
                .dateTime(dto.getDateTime())
                .comment(dto.getComment())
                .difficulty(dto.getDifficulty())
                .distance(dto.getDistance())
                .totalTime(dto.getTotalTime())
                .rating(dto.getRating())
                .tour(tour)
                .build();

        tourLog = tourLogRepository.save(tourLog);
        return tourLogMapper.mapToDto(tourLog);
    }


    @Override
    public List<TourLogDTO> findAll() {
        return tourLogMapper.mapToDto(tourLogRepository.findAll());
    }

    @Override
    public List<TourLogDTO> findById(Long id) {
        checkIfTourLogExist(id);
        TourLogDTO tourLogDTO = tourLogRepository.findById(id)
                .map(tourLogMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("TourLog not found for id " + id));

        // Since the method expects a list, we wrap the single TourDTO in a list.
        return Collections.singletonList(tourLogDTO);
    }

    @Override
    public void deleteById(Long id) {
        checkIfTourLogExist(id);
        tourLogRepository.deleteById(id);
    }

    @Override
    public TourLogDTO update(Long id, TourLogDTO dto) {
        TourLog existingTourLog = tourLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TourLog not found for id " + id));

        // Update the Tour, if the tourId in DTO is different
        if (!existingTourLog.getTour().getId().equals(dto.getTourId())) {
            Tour tour = tourRepository.findById(dto.getTourId())
                    .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + dto.getTourId()));
            existingTourLog.setTour(tour);
        }

        existingTourLog.setDateTime(dto.getDateTime());
        existingTourLog.setComment(dto.getComment());
        existingTourLog.setDifficulty(dto.getDifficulty());
        existingTourLog.setDistance(dto.getDistance());
        existingTourLog.setTotalTime(dto.getTotalTime());
        existingTourLog.setRating(dto.getRating());

        tourLogRepository.save(existingTourLog);
        return tourLogMapper.mapToDto(existingTourLog);
    }
}
