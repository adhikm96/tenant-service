package com.thebizio.biziotenantbaseservice.controller;

import com.thebizio.biziotenantbaseservice.dto.ResponseMessageDto;
import com.thebizio.biziotenantbaseservice.projection.DSPrj;
import com.thebizio.biziotenantbaseservice.service.DSourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data-sources")
public class DSController {

    final DSourceService dSourceService;

    public DSController(DSourceService dSourceService) {
        this.dSourceService = dSourceService;
    }

    @PostMapping("/{tenantId}/{orgCode}")
    public ResponseMessageDto createDS(@PathVariable String tenantId, @PathVariable String orgCode) {
        return new ResponseMessageDto(dSourceService.createDatabase(tenantId, orgCode));
    }

    @GetMapping
    public List<DSPrj> listDataSources() {
        return dSourceService.list();
    }
}
