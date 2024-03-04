package org.wenant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.service.in.AuthService;
import org.wenant.service.in.JwtService;

import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
class LoginControllerTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private LoginController loginController;
    private AuthenticationDto authenticationDto;
    private MockMvc mockMvc;
    private String jsonContent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        authenticationDto = new AuthenticationDto("admin", "admin");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonContent = objectMapper.writeValueAsString(authenticationDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        Mockito.when(authService.authenticateUser(Mockito.any(AuthenticationDto.class))).thenReturn(Optional.of(authenticationDto));
        Mockito.when(jwtService.generateToken(authenticationDto)).thenReturn("generatedToken");
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("generatedToken"));
    }
}