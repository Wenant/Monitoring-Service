package org.wenant.controller;

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
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.service.in.MeterTypeCatalogService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class MeterTypeCatalogControllerTest {
    @Mock
    private MeterTypeCatalogService meterTypeCatalogService;
    @InjectMocks
    private MeterTypeCatalogController meterTypeCatalogController;
    private List<MeterTypeCatalogDto> expectedMeterTypes;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(meterTypeCatalogController).build();
        expectedMeterTypes = Arrays.asList(
                new MeterTypeCatalogDto(0L, "Description1"),
                new MeterTypeCatalogDto(1L, "Description2"));
    }

    @Test
    void testGetMeterTypes() throws Exception {
        when(meterTypeCatalogService.getMeterTypes()).thenReturn(expectedMeterTypes);

        mockMvc.perform(get("/meterTypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedMeterTypes.get(0).getId()))
                .andExpect(jsonPath("$[0].meterType").value(expectedMeterTypes.get(0).getMeterType()))
                .andExpect(jsonPath("$[1].id").value(expectedMeterTypes.get(1).getId()))
                .andExpect(jsonPath("$[1].meterType").value(expectedMeterTypes.get(1).getMeterType()))
                .andReturn();

    }

    @Test
    void testEmptyList() throws Exception {
        when(meterTypeCatalogService.getMeterTypes()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/meterTypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }

    @Test
    void testPostMeterTypes() throws Exception {
        when(meterTypeCatalogService.getMeterTypes()).thenReturn(expectedMeterTypes);
        mockMvc.perform(post("/meterTypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

    }

}