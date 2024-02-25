package org.wenant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.service.in.RegistrationService;

import java.util.Map;

@Audit
@Loggable
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

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