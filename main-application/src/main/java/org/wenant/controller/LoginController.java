package org.wenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.service.in.AuthService;
import org.wenant.service.in.JwtService;

import java.util.Map;
import java.util.Optional;


@RestController
@Tag(name = "Login Controller")
@RequestMapping("/login")
public class LoginController {

    private final JwtService jwtService;
    private final AuthService authService;

    public LoginController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Operation(summary = "Authenticate and generate token")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthenticationDto request) {
        Optional<AuthenticationDto> authResult = authService.authenticateUser(request);

        if (authResult.isPresent()) {
            AuthenticationDto authenticationDto = authResult.get();
            String token = jwtService.generateToken(authenticationDto);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Authentication failed"));
        }
    }
}