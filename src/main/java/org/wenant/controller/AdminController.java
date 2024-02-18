package org.wenant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.service.UserService;
import org.wenant.service.in.JwtService;
import org.wenant.service.in.MeterReadingService;
import org.wenant.service.in.MeterTypeCatalogService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Audit
@Loggable
@RestController
@RequestMapping("/admin/")
public class AdminController {
    private final MeterReadingService meterReadingService;
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final UserService userService;
    private final JwtService jwtService;

    public AdminController(MeterReadingService meterReadingService,
                           MeterTypeCatalogService meterTypeCatalogService,
                           UserService userService, JwtService jwtService) {
        this.meterReadingService = meterReadingService;
        this.meterTypeCatalogService = meterTypeCatalogService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    private ResponseEntity<Map<String, String>> checkToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token format"));
        }

        String token = authorizationHeader.substring(7);
        if (!jwtService.isValidToken(token) || !jwtService.getUserFromToken(token).getRole().equals(UserDto.Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        return null;
    }


    @PostMapping(value = "/meters/types", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMeterType(@RequestHeader(value = "Authorization") String authorizationHeader,
                                             @RequestBody MeterTypeCatalogDto meterTypeCatalogDto) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }

        String meterType = meterTypeCatalogDto.getMeterType();
        if (meterType != null && meterType.length() >= 3) {
            meterTypeCatalogService.addMeterType(meterType);
            return ResponseEntity.ok(Map.of("result", "SUCCESS"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", "Invalid meter type"));
        }

    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization") String authorizationHeader) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }

        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @GetMapping("/{username}/readings/history")
    public ResponseEntity<?> getHistoryByUser(@RequestHeader(value = "Authorization") String authorizationHeader,
                                              @PathVariable("username") String username) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }

        List<MeterReadingDto> meterReadingDtoList = meterReadingService.getAllForUser(username);
        return ResponseEntity.ok(meterReadingDtoList);
    }

    @GetMapping("/{username}/readings/latest")
    public ResponseEntity<?> getLatestByUser(@RequestHeader(value = "Authorization") String authorizationHeader,
                                             @PathVariable("username") String username) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }

        List<MeterReadingDto> meterReadingDtoList = meterReadingService.getLatestMeterReadings(username);
        return ResponseEntity.ok(meterReadingDtoList);
    }

    @GetMapping("/{username}/readings/{year}/{month}")
    public ResponseEntity<?> getReadingsByUserAndDate(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                      @PathVariable("username") String username,
                                                      @PathVariable("year") int year,
                                                      @PathVariable("month") int month) {
        ResponseEntity<?> tokenCheckResult = checkToken(authorizationHeader);
        if (tokenCheckResult != null) {
            return tokenCheckResult;
        }

        List<MeterReadingDto> meterReadingDtoList =
                meterReadingService.getByUserAndDate(username, YearMonth.of(year, month));
        return ResponseEntity.ok(meterReadingDtoList);
    }


}
