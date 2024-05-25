package com.tourplanner.backend.service.report;

import com.tourplanner.backend.service.dto.report.ReportResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final TourReportGenerator tourReportGenerator;

    private final SummarizeReportGenerator summarizeReportGenerator;

    public ReportResponseDTO generateTourReport(Long tourId) {
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
