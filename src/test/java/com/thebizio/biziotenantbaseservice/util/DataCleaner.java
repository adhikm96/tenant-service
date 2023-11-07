package com.thebizio.biziotenantbaseservice.util;

import com.thebizio.biziotenantbaseservice.repo.TenantRepo;
import com.thebizio.biziotenantbaseservice.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataCleaner {

    @Autowired
    TenantRepo tenantRepo;

    public void clean() {
       tenantRepo.deleteAll();
    }
}
