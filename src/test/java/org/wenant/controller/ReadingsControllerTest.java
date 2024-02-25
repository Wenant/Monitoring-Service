package org.wenant.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.service.in.JwtService;
import org.wenant.service.in.MeterReadingService;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class ReadingsControllerTest {
    @Mock
    private MeterReadingService meterReadingService;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private ReadingsController readingsController;
    private MockMvc mockMvc;
    private String validToken;
    private List<MeterReadingDto> expectedReadings;
    private String username;
    private ReadingDto expectedReadingDto;
    private ObjectMapper objectMapper;
    private String jsonContent;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(readingsController).build();
        validToken = "mockedToken";
        username = "mockedUser";
        expectedReadings = Arrays.asList(
                new MeterReadingDto("test1", "hot", 100.0, "2024/02"),
                new MeterReadingDto("test2", "hot", 200.0, "2023/02"));

        expectedReadingDto = new ReadingDto(1L, 20.0);
        objectMapper = new ObjectMapper();
        try {
            jsonContent = objectMapper.writeValueAsString(expectedReadingDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        when(jwtService.isValidToken(validToken)).thenReturn(true);
        when(jwtService.getUsernameFromToken(validToken)).thenReturn(username);
    }

    @Test
    void testGetHistorySuccess() throws Exception {
        when(meterReadingService.getAllForUser(username)).thenReturn(expectedReadings);
        String authorizationHeader = "Bearer " + validToken;

        mockMvc.perform(get("/readings/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value(expectedReadings.get(0).getUsername()))
                .andExpect(jsonPath("$[1].username").value(expectedReadings.get(1).getUsername()))
                .andExpect(jsonPath("$[0].meterType").value(expectedReadings.get(0).getMeterType()))
                .andExpect(jsonPath("$[1].meterType").value(expectedReadings.get(1).getMeterType()))
                .andExpect(jsonPath("$[0].value").value(expectedReadings.get(0).getValue()))
                .andExpect(jsonPath("$[1].value").value(expectedReadings.get(1).getValue()))
                .andExpect(jsonPath("$[0].date").value(expectedReadings.get(0).getDate()))
                .andExpect(jsonPath("$[1].date").value(expectedReadings.get(1).getDate()))
                .andReturn();
    }

    @Test
    void testGetLatestSuccess() throws Exception {
        when(meterReadingService.getLatestMeterReadings(username)).thenReturn(expectedReadings);
        String authorizationHeader = "Bearer " + validToken;

        mockMvc.perform(get("/readings/latest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value(expectedReadings.get(0).getUsername()))
                .andExpect(jsonPath("$[1].username").value(expectedReadings.get(1).getUsername()))
                .andExpect(jsonPath("$[0].meterType").value(expectedReadings.get(0).getMeterType()))
                .andExpect(jsonPath("$[1].meterType").value(expectedReadings.get(1).getMeterType()))
                .andExpect(jsonPath("$[0].value").value(expectedReadings.get(0).getValue()))
                .andExpect(jsonPath("$[1].value").value(expectedReadings.get(1).getValue()))
                .andExpect(jsonPath("$[0].date").value(expectedReadings.get(0).getDate()))
                .andExpect(jsonPath("$[1].date").value(expectedReadings.get(1).getDate()))
                .andReturn();
    }

    @Test
    void testGetByYearAndMonthSuccess() throws Exception {
        expectedReadings = Arrays.asList(
                new MeterReadingDto("test1", "hot", 100.0, "2023/11"),
                new MeterReadingDto("test2", "hot", 200.0, "2023/11"));

        when(meterReadingService.getByUserAndDate(username, YearMonth.of(2023, 11))).thenReturn(expectedReadings);
        String authorizationHeader = "Bearer " + validToken;

        mockMvc.perform(get("/readings/{year}/{month}", 2023, 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value(expectedReadings.get(0).getUsername()))
                .andExpect(jsonPath("$[1].username").value(expectedReadings.get(1).getUsername()))
                .andExpect(jsonPath("$[0].meterType").value(expectedReadings.get(0).getMeterType()))
                .andExpect(jsonPath("$[1].meterType").value(expectedReadings.get(1).getMeterType()))
                .andExpect(jsonPath("$[0].value").value(expectedReadings.get(0).getValue()))
                .andExpect(jsonPath("$[1].value").value(expectedReadings.get(1).getValue()))
                .andExpect(jsonPath("$[0].date").value(expectedReadings.get(0).getDate()))
                .andExpect(jsonPath("$[1].date").value(expectedReadings.get(1).getDate()))
                .andReturn();
    }

    @Test
    void testPostNewReadingSuccess() throws Exception {
        when(meterReadingService.addNewReading(any(ReadingDto.class), eq(username))).thenReturn(MeterReadingService.MeterReadingStatus.SUCCESS);

        String authorizationHeader = "Bearer " + validToken;

        mockMvc.perform(post("/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andReturn();
    }

}