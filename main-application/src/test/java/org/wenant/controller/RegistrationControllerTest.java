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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.domain.dto.ResponseDto;
import org.wenant.service.in.RegistrationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class RegistrationControllerTest {
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private RegistrationController registrationController;
    private RegistrationDto commonRegistrationDto;
    private MockMvc mockMvc;
    private String jsonContent;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        commonRegistrationDto = new RegistrationDto("test", "test");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonContent = objectMapper.writeValueAsString(commonRegistrationDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRegistration_Success() throws Exception {
        when(registrationService.registerUser(any())).thenReturn(RegistrationService.RegistrationResult.SUCCESS);
        ResponseDto responseDto = new ResponseDto("result", "SUCCESS");

        MvcResult mvcResult = mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("SUCCESS"))
                .andReturn();
    }

    @Test
    void testRegistration_INVALID_PASSWORD() throws Exception {
        when(registrationService.registerUser(any())).thenReturn(RegistrationService.RegistrationResult.INVALID_PASSWORD);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("INVALID_PASSWORD"))
                .andReturn();
    }

    @Test
    void testRegistration_INVALID_USERNAME() throws Exception {
        when(registrationService.registerUser(any())).thenReturn(RegistrationService.RegistrationResult.INVALID_USERNAME);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(jsonPath("message").value("INVALID_USERNAME"))
                .andReturn();
    }

    @Test
    void testRegistration_USERNAME_ALREADY_EXISTS() throws Exception {
        when(registrationService.registerUser(any())).thenReturn(RegistrationService.RegistrationResult.USERNAME_ALREADY_EXISTS);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(jsonPath("message").value("USERNAME_ALREADY_EXISTS"))
                .andReturn();
    }

}