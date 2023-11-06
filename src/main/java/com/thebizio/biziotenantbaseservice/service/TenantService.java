package com.thebizio.biziotenantbaseservice.service;

import com.thebizio.biziotenantbaseservice.entity.Tenant;
import com.thebizio.biziotenantbaseservice.exception.ValidationException;
import com.thebizio.biziotenantbaseservice.projection.TenantProjection;
import com.thebizio.biziotenantbaseservice.repo.TenantRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    final DBUtil dbUtil;

    final TenantRepo tenantRepo;

    final CryptoService cryptoService;

    final String DRIVER_CLASS_NAME = "org.postgresql.Driver";

    final DBQueryHelper dbQueryHelper;

    public TenantService(DBUtil dbUtil, TenantRepo tenantRepo, CryptoService cryptoService, DBQueryHelper dbQueryHelper) {
        this.dbUtil = dbUtil;
        this.tenantRepo = tenantRepo;
        this.cryptoService = cryptoService;
        this.dbQueryHelper = dbQueryHelper;
    }

    @Transactional
    public Tenant createTenant(String tenantId, String orgCode, String appCode) {
        Tenant tenant = new Tenant();

        if(tenantRepo.existsByTenantId(tenantId)) {
            throw new ValidationException("tenantId already exists");
        }

        tenant.setTenantId(tenantId);
        tenant.setOrgCode(orgCode);
        tenant.setAppCode(appCode);
        tenant.setUsername(username);
        tenant.setPassword(cryptoService.encrypt(password));
        tenant.setUrl(dbUtil.getDbUrl(tenantId));
        tenant.setDriverClassName(DRIVER_CLASS_NAME);

        tenantRepo.save(tenant);

        if(!dbQueryHelper.createDataBase(tenant.getTenantId())) {
            throw new ValidationException("failed to create tenant");
        }

        return tenant;
    }

    public List<TenantProjection> list(Optional<String> appCode) {
        if(appCode.isPresent())
            return tenantRepo.fetchAllByAppCode(appCode);
        return tenantRepo.fetchAll();
    }
}
