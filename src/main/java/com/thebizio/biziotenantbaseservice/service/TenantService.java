package com.thebizio.biziotenantbaseservice.service;

import com.thebizio.biziotenantbaseservice.entity.Tenant;
import com.thebizio.biziotenantbaseservice.exception.ValidationException;
import com.thebizio.biziotenantbaseservice.projection.TenantProjection;
import com.thebizio.biziotenantbaseservice.repo.TenantRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantService {

    final DBUtil dbUtil;

    final TenantRepo tenantRepo;

    final CryptoService cryptoService;

    final String DRIVER_CLASS_NAME = "org.postgresql.Driver";

    final DBQueryHelper dbQueryHelper;

    final StrUtil strUtil;
    private final String DB_NAME_PREFIX = "salon-db-";

    public TenantService(DBUtil dbUtil, TenantRepo tenantRepo, CryptoService cryptoService, DBQueryHelper dbQueryHelper, StrUtil strUtil) {
        this.dbUtil = dbUtil;
        this.tenantRepo = tenantRepo;
        this.cryptoService = cryptoService;
        this.dbQueryHelper = dbQueryHelper;
        this.strUtil = strUtil;
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
        tenant.setUsername(DB_NAME_PREFIX + orgCode);

        System.out.println(strUtil.getRandomDBPassword());
        tenant.setPassword(cryptoService.encrypt(strUtil.getRandomDBPassword()));
        tenant.setUrl(dbUtil.getDbUrl(tenantId));
        tenant.setDriverClassName(DRIVER_CLASS_NAME);

        tenantRepo.save(tenant);

        if(!dbQueryHelper.createDataBaseAndUser(tenant.getTenantId(), tenant.getUsername(), cryptoService.decrypt(tenant.getPassword()))) {
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
