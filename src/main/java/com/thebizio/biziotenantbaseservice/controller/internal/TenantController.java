package com.thebizio.biziotenantbaseservice.controller.internal;

import com.thebizio.biziotenantbaseservice.dto.ResponseMessageDto;
import com.thebizio.biziotenantbaseservice.projection.TenantProjection;
import com.thebizio.biziotenantbaseservice.service.TenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/internal/tenants")
public class TenantController {

    final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/{tenantId}/{orgCode}/{appCode}")
    public ResponseMessageDto createTenant(@PathVariable String tenantId, @PathVariable String orgCode, @PathVariable String appCode) {
        return new ResponseMessageDto(tenantService.createTenant(tenantId, orgCode, appCode).getId().toString());
    }

    @GetMapping
    public List<TenantProjection> listTenants(@RequestParam Optional<String> appCode) {
        return tenantService.list(appCode);
    }
}
