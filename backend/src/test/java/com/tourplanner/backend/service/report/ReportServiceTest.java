package com.tourplanner.backend.service.report;

import com.tourplanner.backend.persistence.repository.TourRepository;
import com.tourplanner.backend.service.dto.report.ReportResponseDTO;
import com.tourplanner.backend.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private TourReportGenerator tourReportGenerator;

    @Mock
    private SummarizeReportGenerator summarizeReportGenerator;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTourReport_TourExists() {
        when(tourRepository.existsById(1L)).thenReturn(true);
        when(tourReportGenerator.generateReport()).thenReturn("http://example.com/tourReport.pdf");

        ReportResponseDTO response = reportService.generateTourReport(1L);

        assertNotNull(response);
        assertEquals("http://example.com/tourReport.pdf", response.getReportURL());
        verify(tourRepository, times(1)).existsById(1L);
        verify(tourReportGenerator, times(1)).generateReport();
    }

    @Test
    void generateTourReport_TourNotExists() {
        when(tourRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reportService.generateTourReport(1L));
        verify(tourRepository, times(1)).existsById(1L);
        verify(tourReportGenerator, times(0)).generateReport();
    }

    @Test
    void generateSummarizeReport() {
        when(summarizeReportGenerator.generateReport()).thenReturn("http://example.com/summarizeReport.pdf");

        ReportResponseDTO response = reportService.generateSummarizeReport();

        assertNotNull(response);
        assertEquals("http://example.com/summarizeReport.pdf", response.getReportURL());
        verify(summarizeReportGenerator, times(1)).generateReport();
    }
}
