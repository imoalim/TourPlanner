package com.tourplanner.backend.controller;

import com.tourplanner.backend.service.dto.report.ReportResponseDTO;
import com.tourplanner.backend.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/tours/{id}/report")
    public ResponseEntity<ReportResponseDTO> getTourReport(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(reportService.generateTourReport(id));
    }

    @GetMapping("/tours/summarize-report")
    public ResponseEntity<ReportResponseDTO> getSummarizeReport() throws Exception {
        return ResponseEntity.ok(reportService.generateSummarizeReport());
    }
}
