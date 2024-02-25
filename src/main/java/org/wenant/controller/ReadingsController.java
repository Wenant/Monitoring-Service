package org.wenant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.service.in.JwtService;
import org.wenant.service.in.MeterReadingService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Audit
@Loggable
@RestController
@RequestMapping("/readings")
public class ReadingsController {

    private final MeterReadingService meterReadingService;
    private final JwtService jwtService;

    public ReadingsController(MeterReadingService meterReadingService, JwtService jwtService) {
        this.meterReadingService = meterReadingService;
        this.jwtService = jwtService;
    }

    private ResponseEntity<?> checkToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token format"));
        }

        String token = authorizationHeader.substring(7);
        if (!jwtService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postNewReading(@RequestHeader(value = "Authorization") String authorizationHeader,
                                            @RequestBody ReadingDto readingDto) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);

        MeterReadingService.MeterReadingStatus status = meterReadingService.addNewReading(readingDto, username);

        switch (status) {
            case SUCCESS:
                return ResponseEntity.ok(Map.of("result", status));
            case ALREADY_EXISTS:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("result", status));
            case INVALID_TYPE_ID:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", status));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result", status));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@RequestHeader(value = "Authorization") String authorizationHeader) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);

        List<MeterReadingDto> meterReadingDtoList = meterReadingService.getAllForUser(username);
        return ResponseEntity.ok(meterReadingDtoList);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatest(@RequestHeader(value = "Authorization") String authorizationHeader) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);

        List<MeterReadingDto> meterReadingDtoList = meterReadingService.getLatestMeterReadings(username);
        return ResponseEntity.ok(meterReadingDtoList);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<?> getByYearAndMonth(@RequestHeader(value = "Authorization") String authorizationHeader,
                                               @PathVariable("year") int year,
                                               @PathVariable("month") int month) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }
        String token = authorizationHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);

        List<MeterReadingDto> meterReadingDtoList =
                meterReadingService.getByUserAndDate(username, YearMonth.of(year, month));
        return ResponseEntity.ok(meterReadingDtoList);
    }

}