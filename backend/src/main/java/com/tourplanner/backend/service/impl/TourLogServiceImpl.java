package com.tourplanner.backend.service.impl;

import com.tourplanner.backend.persistence.entity.Tour;
import com.tourplanner.backend.persistence.entity.TourLog;
import com.tourplanner.backend.persistence.repository.TourLogRepository;
import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.GenericService;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.mapper.TourLogMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TourLogServiceImpl implements GenericService<TourLogDTO, Long> {

    private final TourLogRepository tourLogRepository;

    private final TourLogMapper tourLogMapper;

    private final TourRepository tourRepository;

    private final TourServiceImpl tourServiceImpl;

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
        tourServiceImpl.updateComputedTourAttributes(tour.getId());

        return tourLogMapper.mapToDto(tourLog);
    }


    @Override
    public List<TourLogDTO> findAll() {
        return tourLogMapper.mapToDto(tourLogRepository.findAll());
    }

    @Override
    public TourLogDTO findById(Long id) {
        checkIfTourLogExist(id);
        return tourLogRepository.findById(id)
                .map(tourLogMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("TourLog not found for id " + id));
    }

    @Override
    public void deleteById(Long tourLogId) {
        checkIfTourLogExist(tourLogId);
        tourServiceImpl.updateComputedTourAttributes(getTourIDFromTourLogID(tourLogId));
        tourLogRepository.deleteById(tourLogId);
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
        tourServiceImpl.updateComputedTourAttributes(existingTourLog.getTour().getId());

        return tourLogMapper.mapToDto(existingTourLog);
    }

    public Long getTourIDFromTourLogID(Long tourLogId) {
        TourLog tourLog = tourLogRepository.findById(tourLogId)
                .orElseThrow(() -> new EntityNotFoundException("TourLog not found for tourLogId " + tourLogId));

        return tourLog.getTour().getId();
    }
}
