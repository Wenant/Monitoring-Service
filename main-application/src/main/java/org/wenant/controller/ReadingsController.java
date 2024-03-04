package org.wenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.ReadingDto;
import org.wenant.domain.dto.ResponseDto;
import org.wenant.service.in.AccessControlService;
import org.wenant.service.in.MeterReadingService;

import java.time.YearMonth;
import java.util.List;


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
    @Operation(
            summary = "Add a new meter reading",
            parameters = {
                    @Parameter(name = "Authorization", description = "Bearer token for authentication", required = true)
            })
    public ResponseEntity<?> postNewReading(@RequestHeader(value = "Authorization") String authorizationHeader,
                                            @RequestBody ReadingDto readingDto) {
        MeterReadingService.MeterReadingStatus status = accessControlService.addNewReading(readingDto, authorizationHeader);
        ResponseDto responseDto = new ResponseDto("result", status.toString());

        switch (status) {
            case SUCCESS:
                return ResponseEntity.ok(responseDto);
            case ALREADY_EXISTS:
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDto);
            case INVALID_TYPE_ID, INVALID_TOKEN:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    @GetMapping({"/history", "/history/{username}"})
    @Operation(
            summary = "Get meter reading history",
            description = "Retrieve meter reading history. Use '/history' for regular users and '/history/{username}' for admins."
    )
    public ResponseEntity<?> getHistory(@RequestHeader(value = "Authorization") String authorizationHeader,
                                        @PathVariable(name = "username", required = false) String usernameFromPath) {
        try {
            List<MeterReadingDto> meterReadingDtoList =
                    accessControlService.getAllMeterReadings(authorizationHeader, usernameFromPath);
            return ResponseEntity.ok(meterReadingDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("error", e.getMessage()));
        }
    }

    @GetMapping({"/latest", "/latest/{username}"})
    @Operation(
            summary = "Get latest meter reading",
            description = "Retrieve latest meter reading. Use '/latest' for regular users and '/latest/{username}' for admins."
    )
    public ResponseEntity<?> getLatest(@RequestHeader(value = "Authorization") String authorizationHeader,
                                       @PathVariable(name = "username", required = false) String usernameFromPath) {
        try {
            List<MeterReadingDto> meterReadingDtoList =
                    accessControlService.getLatestMeterReadings(authorizationHeader, usernameFromPath);
            return ResponseEntity.ok(meterReadingDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("error", e.getMessage()));
        }
    }

    @GetMapping({"/{year}/{month}", "/{year}/{month}/{username}"})
    @Operation(
            summary = "Get meter reading by date",
            description = "Retrieve meter reading by date. Use '/{year}/{month}' for regular users and '/{year}/{month}/{username}' for admins."
    )
    public ResponseEntity<?> getByYearAndMonth(@RequestHeader(value = "Authorization") String authorizationHeader,
                                               @PathVariable("year") int year,
                                               @PathVariable("month") int month,
                                               @PathVariable(name = "username", required = false) String usernameFromPath) {

        try {
            List<MeterReadingDto> meterReadingDtoList =
                    accessControlService.getByUserAndDate(authorizationHeader, usernameFromPath, YearMonth.of(year, month));
            return ResponseEntity.ok(meterReadingDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("error", e.getMessage()));
        }
    }

}