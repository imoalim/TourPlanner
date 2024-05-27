package com.tourplanner.backend.controller;

import com.tourplanner.backend.service.dto.report.ReportResponseDTO;
import com.tourplanner.backend.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/tours/{id}/report")
    public ResponseEntity<ReportResponseDTO> getTourReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.generateTourReport(id));
    }

    @GetMapping("/tours/summarize-report")
    public ResponseEntity<ReportResponseDTO> getSummarizeReport() {
        return ResponseEntity.ok(reportService.generateSummarizeReport());
    }
}
