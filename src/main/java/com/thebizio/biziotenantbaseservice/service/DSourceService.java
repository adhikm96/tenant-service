package com.thebizio.biziotenantbaseservice.service;

import com.thebizio.biziotenantbaseservice.entity.DataSource;
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

    public DSourceService(DBUtil dbUtil, DSRepo dsRepo, CryptoService cryptoService) {
        this.dbUtil = dbUtil;
        this.dsRepo = dsRepo;
        this.cryptoService = cryptoService;
    }

    public String createDatabase(String tenantId, String orgCode) {
        DataSource dataSource = new DataSource();
        dataSource.setTenantId(tenantId);
        dataSource.setOrgCode(orgCode);

        dataSource.setUsername(username);
        dataSource.setPassword(cryptoService.encrypt(password));
        dataSource.setUrl(dbUtil.getDbUrl(tenantId));
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dsRepo.save(dataSource).getId().toString();
    }

    public List<DSPrj> list() {
        return dsRepo.fetchAll();
    }
}
