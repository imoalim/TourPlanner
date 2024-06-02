package com.tourplanner.backend.controller;

import com.tourplanner.backend.service.dto.report.ReportResponseDTO;
import com.tourplanner.backend.service.exception.ResourceNotFoundException;
import com.tourplanner.backend.service.report.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    private static final String TOUR_REPORT_API = "/api/v1/tours/{id}/report";
    private static final String SUMMARIZE_REPORT_API = "/api/v1/tours/summarize-report";

    // Verifying HTTP
    @Test
    void whenGetTourReportHasValidInput_thenReturn200() throws Exception {
        ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                .reportURL("http://example.com/report")
                .build();

        when(reportService.generateTourReport(1L)).thenReturn(reportResponseDTO);
        mockMvc.perform(get(TOUR_REPORT_API, 1L)).andExpect(status().isOk());
    }

    @Test
    void whenGetTourReportHasInvalidInput_thenReturn404() throws Exception {
        when(reportService.generateTourReport(anyLong())).thenThrow(new ResourceNotFoundException("Not found tour with id 2"));
        mockMvc.perform(get(TOUR_REPORT_API, 2L)).andExpect(status().isNotFound());
    }

    // Verifying Business Logic Calls
    @Test
    void whenCreateTourReportHasValidInput_thenCallService() throws Exception {
        ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                .reportURL("http://example.com/report")
                .build();

        mockMvc.perform(get(TOUR_REPORT_API, 1L)).andExpect(status().isOk());
        when(reportService.generateTourReport(1L)).thenReturn(reportResponseDTO);
        verify(reportService, times(1)).generateTourReport(1L);
    }

    @Test
    void whenCreateSummarizeReport_thenCallService() throws Exception {
        ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                .reportURL("http://example.com/report")
                .build();

        mockMvc.perform(get(SUMMARIZE_REPORT_API)).andExpect(status().isOk());
        when(reportService.generateSummarizeReport()).thenReturn(reportResponseDTO);
        verify(reportService, times(1)).generateSummarizeReport();
    }

}
