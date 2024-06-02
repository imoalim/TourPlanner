package com.tourplanner.backend.service.report;

import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.report.ReportResponseDTO;
import com.tourplanner.backend.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final TourRepository tourRepository;

    private final TourReportGenerator tourReportGenerator;

    private final SummarizeReportGenerator summarizeReportGenerator;

    public ReportResponseDTO generateTourReport(Long tourId) {
        if (!tourRepository.existsById(tourId)) {
            throw new ResourceNotFoundException("Tour not found with id " + tourId);
        }

        tourReportGenerator.setTourID(tourId);
        String reportURL = tourReportGenerator.generateReport();

        return ReportResponseDTO.builder()
                .reportURL(reportURL)
                .build();
    }

    public ReportResponseDTO generateSummarizeReport() {
        String reportURL = summarizeReportGenerator.generateReport();

        return ReportResponseDTO.builder()
                .reportURL(reportURL)
                .build();
    }
}
