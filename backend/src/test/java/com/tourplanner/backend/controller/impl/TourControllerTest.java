package com.tourplanner.backend.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tourplanner.backend.persistence.attributes.tour.ChildFriendliness;
import com.tourplanner.backend.persistence.attributes.tour.Popularity;
import com.tourplanner.backend.service.dto.map.MapInfoDTO;
import com.tourplanner.backend.service.dto.tour.TourDTO;
import com.tourplanner.backend.service.impl.TourServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TourController.class)
class TourControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TourServiceImpl tourService;

    private static final String TOUR_API = "/api/v1/tours";

    // Verifying Input Deserialization
    @Test
    void whenCreateTourHasValidInput_thenReturn200() throws Exception {
        TourDTO tourDTO = generateValidTourDTO();

        mockMvc.perform(post(TOUR_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tourDTO)))
                .andExpect(status().isOk());
    }

    // Verifying Input Validation
    @Test
    void whenCreateTourHasNullValue_thenReturn400() throws Exception {
        TourDTO tourDTO = TourDTO.builder()
                .name(null)
                .fromLocation("Austria, 1030 Wien, Schnirchgasse 13")
                .toLocation("Austria, 1020 Wien, Praterstern 5")
                .transportType("foot-walking")
                .build();

        mockMvc.perform(post(TOUR_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tourDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("name cannot be empty"));
    }

    // Verifying Business Logic Calls
    @Test
    void whenCreateTourHasValidInput_thenCallsService() throws Exception {
        TourDTO tourDTO = generateValidTourDTO();

        mockMvc.perform(post(TOUR_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tourDTO)))
                .andExpect(status().isOk());

        ArgumentCaptor<TourDTO> tourDTOArgumentCaptor = ArgumentCaptor.forClass(TourDTO.class);
        verify(tourService, times(1)).create(tourDTOArgumentCaptor.capture());

        assertEquals(tourDTOArgumentCaptor.getValue().getName(), "Lazy walk");
        assertEquals(tourDTOArgumentCaptor.getValue().getFromLocation(), "Austria, 1020 Wien, Praterstern 6");
        assertEquals(tourDTOArgumentCaptor.getValue().getToLocation(), "Austria, 1020 Wien, Praterstern 5");
        assertEquals(tourDTOArgumentCaptor.getValue().getTransportType(), "foot-walking");
    }

    // Verifying Output Serialization
    @Test
    void whenCreateTourHasValidInput_thenReturnTourResource() throws Exception {
        TourDTO tourDTO = generateValidTourDTO();

        TourDTO expectedResponseBody = TourDTO.builder()
                .name("Lazy walk")
                .fromLocation("Austria, 1020 Wien, Praterstern 6")
                .toLocation("Austria, 1020 Wien, Praterstern 5")
                .transportType("foot-walking")
                .distance(209.9)
                .estimatedTime(151.1)
                .mapInfoDTO(MapInfoDTO.builder()
                        .startLat(48.217011)
                        .startLng(16.391565)
                        .endLat(48.217861)
                        .endLng(16.393175)
                        .centerLat(48.2174075)
                        .centerLng(16.39237)
                        .route("[[48.217011, 16.391565], [48.216954, 16.391619], [48.216967, 16.391657], [48.216981, 16.391697], " +
                                "[48.217093, 16.39177], [48.217215, 16.391768], [48.217326, 16.39184], [48.217427, 16.391893], " +
                                "[48.21778, 16.391854], [48.217823, 16.391849], [48.217808, 16.392142], [48.217826, 16.392515], " +
                                "[48.217861, 16.393175]]")
                        .build())
                .popularity(Popularity.UNKNOWN)
                .childFriendliness(ChildFriendliness.UNKNOWN)
                .build();

        when(tourService.create(any(TourDTO.class))).thenReturn(expectedResponseBody);

        mockMvc.perform(post(TOUR_API)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tourDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lazy walk"))
                .andExpect(jsonPath("$.fromLocation").value("Austria, 1020 Wien, Praterstern 6"))
                .andExpect(jsonPath("$.toLocation").value("Austria, 1020 Wien, Praterstern 5"))
                .andExpect(jsonPath("$.transportType").value("foot-walking"))
                .andExpect(jsonPath("$.distance").value(209.9))
                .andExpect(jsonPath("$.estimatedTime").value(151.1))
                .andExpect(jsonPath("$.mapInfoDTO.startLat").value(48.217011))
                .andExpect(jsonPath("$.mapInfoDTO.startLng").value(16.391565))
                .andExpect(jsonPath("$.mapInfoDTO.endLat").value(48.217861))
                .andExpect(jsonPath("$.mapInfoDTO.endLng").value(16.393175))
                .andExpect(jsonPath("$.mapInfoDTO.centerLat").value(48.2174075))
                .andExpect(jsonPath("$.mapInfoDTO.centerLng").value(16.39237))
                .andExpect(jsonPath("$.mapInfoDTO.route").value("[[48.217011, 16.391565], [48.216954, 16.391619], " +
                        "[48.216967, 16.391657], [48.216981, 16.391697], " +
                        "[48.217093, 16.39177], [48.217215, 16.391768], [48.217326, 16.39184], [48.217427, 16.391893], " +
                        "[48.21778, 16.391854], [48.217823, 16.391849], [48.217808, 16.392142], [48.217826, 16.392515], " +
                        "[48.217861, 16.393175]]"))
                .andExpect(jsonPath("$.popularity").value("UNKNOWN"))
                .andExpect(jsonPath("$.childFriendliness").value("UNKNOWN"));
    }

    private TourDTO generateValidTourDTO() {
        return TourDTO.builder()
                .name("Lazy walk")
                .fromLocation("Austria, 1020 Wien, Praterstern 6")
                .toLocation("Austria, 1020 Wien, Praterstern 5")
                .transportType("foot-walking")
                .build();
    }
}