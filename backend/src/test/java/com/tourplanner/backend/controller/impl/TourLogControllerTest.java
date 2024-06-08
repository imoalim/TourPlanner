package com.tourplanner.backend.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import com.tourplanner.backend.service.dto.tourLog.TourLogDTO;
import com.tourplanner.backend.service.impl.TourLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TourLogController.class)
class TourLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TourLogServiceImpl tourLogService;

    private static final String TOURLOG_API = "/api/v1/tourLogs";

    private TourLogDTO validTourLogDTO;

    @BeforeEach
    void setUp() {
        TourDTO validTourDTO = TourDTO.builder()
                .id(1L)
                .name("Lazy walk")
                .fromLocation("Austria, 1030 Wien, Schnirchgasse 13")
                .toLocation("Austria, 1020 Wien, Praterstern 5")
                .transportType("foot-walking")
                .build();

        validTourLogDTO = TourLogDTO.builder()
                .id(1L)
                .comment("Great tour")
                .tourId(1L)
                .build();
    }

    // Verifying Input Deserialization
    @Test
    void whenCreateTourLogHasValidInput_thenReturn200() throws Exception {
        mockMvc.perform(post(TOURLOG_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTourLogDTO)))
                .andExpect(status().isOk());
    }

    // Verifying Input Validation
    @Test
    void whenCreateTourLogHasNullTourId_thenReturn400() throws Exception {
        TourLogDTO nullTourLogDTO = TourLogDTO.builder()
                .comment("Great tour")
                .tourId(null)
                .build();

        mockMvc.perform(post(TOURLOG_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullTourLogDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.tourId").value("tourId cannot be null"));
    }

    // Verifying Business Logic Calls
    @Test
    void whenCreateHasValidInput_thenCallsService() throws Exception {
        mockMvc.perform(post(TOURLOG_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTourLogDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<TourLogDTO> tourLogDTOArgumentCaptor = ArgumentCaptor.forClass(TourLogDTO.class);
        verify(tourLogService, times(1)).create(tourLogDTOArgumentCaptor.capture());
        assertEquals(tourLogDTOArgumentCaptor.getValue().getId(), 1L);
        assertEquals(tourLogDTOArgumentCaptor.getValue().getComment(), "Great tour");
        assertEquals(tourLogDTOArgumentCaptor.getValue().getTourId(), 1L);
    }
}