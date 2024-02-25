package org.wenant.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.service.in.AccessControlService;
import org.wenant.service.in.MeterReadingService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;


@RestController
@Tag(name = "Readings Controller")
@RequestMapping("/readings")
public class ReadingsController {

    private final MeterReadingService meterReadingService;
    private final AccessControlService accessControlService;

    public ReadingsController(MeterReadingService meterReadingService, AccessControlService accessControlService) {
        this.meterReadingService = meterReadingService;
        this.accessControlService = accessControlService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postNewReading(@RequestHeader(value = "Authorization") String authorizationHeader,
                                            @RequestBody ReadingDto readingDto) {
        MeterReadingService.MeterReadingStatus status = accessControlService.addNewReading(readingDto, authorizationHeader);

        switch (status) {
            case SUCCESS:
                return ResponseEntity.ok(Map.of("result", status));
            case ALREADY_EXISTS:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("result", status));
            case INVALID_TYPE_ID, INVALID_TOKEN:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", status));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("result", status));
        }
    }

    @GetMapping({"/history", "/history/{username}"})
    public ResponseEntity<?> getHistory(@RequestHeader(value = "Authorization") String authorizationHeader,
                                        @PathVariable(name = "username", required = false) String usernameFromPath) {
        try {
            List<MeterReadingDto> meterReadingDtoList =
                    accessControlService.getAllMeterReadings(authorizationHeader, usernameFromPath);
            return ResponseEntity.ok(meterReadingDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping({"/latest", "/latest/{username}"})
    public ResponseEntity<?> getLatest(@RequestHeader(value = "Authorization") String authorizationHeader,
                                       @PathVariable(name = "username", required = false) String usernameFromPath) {
        try {
            List<MeterReadingDto> meterReadingDtoList =
                    accessControlService.getLatestMeterReadings(authorizationHeader, usernameFromPath);
            return ResponseEntity.ok(meterReadingDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping({"/{year}/{month}", "/{year}/{month}/{username}"})
    public ResponseEntity<?> getByYearAndMonth(@RequestHeader(value = "Authorization") String authorizationHeader,
                                               @PathVariable("year") int year,
                                               @PathVariable("month") int month,
                                               @PathVariable(name = "username", required = false) String usernameFromPath) {

        try {
            List<MeterReadingDto> meterReadingDtoList =
                    accessControlService.getByUserAndDate(authorizationHeader, usernameFromPath, YearMonth.of(year, month));
            return ResponseEntity.ok(meterReadingDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

}