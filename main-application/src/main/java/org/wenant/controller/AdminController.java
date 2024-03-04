package org.wenant.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.dto.ResponseDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.service.in.AccessControlService;

import java.util.List;


@RestController
@Tag(name = "Admin Controller")
@RequestMapping("/admin/")
public class AdminController {
    private final AccessControlService accessControlService;
    public AdminController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }


    @PostMapping(value = "/meters/types", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Add new meter type",
            description = "Add a new meter type. Requires admin authorization.")
    public ResponseEntity<?> addNewMeterType(@RequestHeader(value = "Authorization") String authorizationHeader,
                                             @RequestBody MeterTypeCatalogDto meterTypeCatalogDto) {
        try {
            accessControlService.addMeterType(meterTypeCatalogDto, authorizationHeader);
            return ResponseEntity.ok(new ResponseDto("result", "SUCCESS"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("error", e.getMessage()));
        }
    }

    @GetMapping("/users")
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users. Requires admin authorization.")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization") String authorizationHeader) {

        try {
            List<UserDto> userDtoList = accessControlService.getAllUsers(authorizationHeader);
            return ResponseEntity.ok(userDtoList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("error", e.getMessage()));
        }
    }


}