package org.wenant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.service.in.MeterTypeCatalogService;

import java.util.List;

@Audit
@Loggable
@RestController
@RequestMapping("/meterTypes")
public class MeterTypeCatalogController {
    private final MeterTypeCatalogService meterTypeCatalogService;

    @Autowired
    public MeterTypeCatalogController(MeterTypeCatalogService meterTypeCatalogService) {
        this.meterTypeCatalogService = meterTypeCatalogService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeterTypeCatalogDto>> getMeterTypes() {
        List<MeterTypeCatalogDto> meterType = meterTypeCatalogService.getMeterTypes();
        return ResponseEntity.ok(meterType);
    }
}