package com.thebizio.biziotenantbaseservice.service;

import com.thebizio.biziotenantbaseservice.entity.DataSource;
import com.thebizio.biziotenantbaseservice.exception.ValidationException;
import com.thebizio.biziotenantbaseservice.projection.DSPrj;
import com.thebizio.biziotenantbaseservice.repo.DSRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DSourceService {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    final DBUtil dbUtil;

    final DSRepo dsRepo;

    final CryptoService cryptoService;

    final String DRIVER_CLASS_NAME = "org.postgresql.Driver";

    public DSourceService(DBUtil dbUtil, DSRepo dsRepo, CryptoService cryptoService) {
        this.dbUtil = dbUtil;
        this.dsRepo = dsRepo;
        this.cryptoService = cryptoService;
    }

    public DataSource createDataSourceEntry(String tenantId, String orgCode, String appCode) {
        DataSource dataSource = new DataSource();

        if(dsRepo.existsByTenantId(tenantId)) {
            throw new ValidationException("tenantId already exists");
        }

        dataSource.setTenantId(tenantId);
        dataSource.setOrgCode(orgCode);
        dataSource.setAppCode(appCode);
        dataSource.setUsername(username);
        dataSource.setPassword(cryptoService.encrypt(password));
        dataSource.setUrl(dbUtil.getDbUrl(tenantId));
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);

        return dsRepo.save(dataSource);
    }

    public List<DSPrj> list() {
        return dsRepo.fetchAll();
    }
}
