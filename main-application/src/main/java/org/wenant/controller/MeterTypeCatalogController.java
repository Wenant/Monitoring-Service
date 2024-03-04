package org.wenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.service.in.MeterTypeCatalogService;

import java.util.List;


@RestController
@Tag(name = "Meter Type Catalog Controller")
@RequestMapping("/meterTypes")
public class MeterTypeCatalogController {
    private final MeterTypeCatalogService meterTypeCatalogService;

    @Autowired
    public MeterTypeCatalogController(MeterTypeCatalogService meterTypeCatalogService) {
        this.meterTypeCatalogService = meterTypeCatalogService;
    }

    @Operation(summary = "Get all meter types")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterTypeCatalogDto>> getMeterTypes() {
        List<MeterTypeCatalogDto> meterType = meterTypeCatalogService.getMeterTypes();
        return ResponseEntity.ok(meterType);
    }
}