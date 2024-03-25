package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourLogRepository;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.GenericService;
import com.tourplanner.backend.service.dto.TourLogDTO;
import com.tourplanner.backend.service.mapper.TourLogMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TourLogServiceImpl implements GenericService<TourLogDTO, Long> {

    private final TourLogRepository tourLogRepository;

    private final TourRepository tourRepository;

    private final TourLogMapper tourLogMapper;

    void checkIfTourLogExist(Long id){
        if (!tourLogRepository.existsById(id))
            throw new EntityNotFoundException("TourLog not found for id " + id);
    }

    @Override
    public TourLogDTO create(TourLogDTO tourLogDTO) {
        Tour tour = tourRepository.findById(tourLogDTO.getTourId())
                .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + tourLogDTO.getTourId()));

        TourLog tourLog = TourLog.builder()
                .dateTime(tourLogDTO.getDateTime())
                .comment(tourLogDTO.getComment())
                .difficulty(tourLogDTO.getDifficulty())
                .distance(tourLogDTO.getDistance())
                .totalTime(tourLogDTO.getTotalTime())
                .rating(tourLogDTO.getRating())
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
    public TourLogDTO update(Long id, TourLogDTO tourLogDTO) {
        TourLog existingTourLog = tourLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TourLog not found for id " + id));

        // Update the Tour, if the tourId in DTO is different
        if (!existingTourLog.getTour().getId().equals(tourLogDTO.getTourId())) {
            Tour tour = tourRepository.findById(tourLogDTO.getTourId())
                    .orElseThrow(() -> new EntityNotFoundException("Tour not found for id " + tourLogDTO.getTourId()));
            existingTourLog.setTour(tour);
        }

        existingTourLog.setDateTime(tourLogDTO.getDateTime());
        existingTourLog.setComment(tourLogDTO.getComment());
        existingTourLog.setDifficulty(tourLogDTO.getDifficulty());
        existingTourLog.setDistance(tourLogDTO.getDistance());
        existingTourLog.setTotalTime(tourLogDTO.getTotalTime());
        existingTourLog.setRating(tourLogDTO.getRating());

        tourLogRepository.save(existingTourLog);
        return tourLogMapper.mapToDto(existingTourLog);
    }
}
