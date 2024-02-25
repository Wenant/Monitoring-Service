package org.wenant.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.service.UserService;
import org.wenant.service.in.AccessControlService;
import org.wenant.service.in.MeterTypeCatalogService;
import org.wenant.starter.annotations.EnableAudit;

import java.util.List;
import java.util.Map;


@RestController
@Tag(name = "Admin Controller")
@RequestMapping("/admin/")
public class AdminController {

    private final MeterTypeCatalogService meterTypeCatalogService;
    private final UserService userService;
    private final AccessControlService accessControlService;

    public AdminController(MeterTypeCatalogService meterTypeCatalogService,
                           UserService userService,
                           AccessControlService accessControlService) {
        this.meterTypeCatalogService = meterTypeCatalogService;
        this.userService = userService;
        this.accessControlService = accessControlService;
    }


    @PostMapping(value = "/meters/types", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMeterType(@RequestHeader(value = "Authorization") String authorizationHeader,
                                             @RequestBody MeterTypeCatalogDto meterTypeCatalogDto) {
        try {
            accessControlService.addMeterType(meterTypeCatalogDto, authorizationHeader);
            return ResponseEntity.ok(Map.of("result", "SUCCESS"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization") String authorizationHeader) {

        try {
            List<UserDto> userDtoList = accessControlService.getAllUsers(authorizationHeader);
            return ResponseEntity.ok(userDtoList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", e.getMessage()));
        }
    }


}