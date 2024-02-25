package org.wenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.service.in.RegistrationService;

import java.util.Map;


@RestController
@Tag(name = "Registration Controller")
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "Create a new user")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> registration(@RequestBody RegistrationDto request) {
        RegistrationService.RegistrationResult result = registrationService.registerUser(request);

        switch (result) {
            case SUCCESS:
                return ResponseEntity.ok(Map.of("result", result));
            case INVALID_USERNAME, INVALID_PASSWORD:
                return ResponseEntity.badRequest().body(Map.of("result", result));
            case USERNAME_ALREADY_EXISTS:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("result", result));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result", result));

        }

    }

}