package com.thebizio.biziotenantbaseservice.controller;

import com.thebizio.biziotenantbaseservice.entity.Tenant;
import com.thebizio.biziotenantbaseservice.repo.TenantRepo;
import com.thebizio.biziotenantbaseservice.service.CryptoService;
import com.thebizio.biziotenantbaseservice.service.TenantService;
import com.thebizio.biziotenantbaseservice.util.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import static org.hamcrest.CoreMatchers.is;

import java.sql.ResultSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TenantControllerTest extends BaseControllerTest {
    @Autowired
    TenantService tenantService;

    @Autowired
    CryptoService cryptoService;

    @Autowired
    TenantRepo tenantRepo;

    @Test
    void test_tenant_create() throws Exception {

        String tenantId = UUID.randomUUID().toString();
        String orgCode =  UUID.randomUUID().toString();
        String appCode =  UUID.randomUUID().toString();

        mvc.perform(mvcReqHelper.setUpWithoutToken(post("/api/v1/internal/tenants/"+ tenantId + "/" + orgCode + "/" + appCode)))
                .andExpect(status().isOk());

        Tenant tenant = tenantRepo.findAll().get(0);

        assertNotNull(tenant);

        // checking db is created & created user has access to it
        DataSource dataSource = DataSourceBuilder.create()
                .username(tenant.getUsername())
                .password(cryptoService.decrypt(tenant.getPassword()))
                .url(tenant.getUrl())
                .build();

        ResultSet resultSet = dataSource.getConnection().prepareStatement("select current_database() as dbName").executeQuery();
        while (resultSet.next()) {
            assertEquals(resultSet.getString("dbName"), tenant.getTenantId());
        }

        assertNotNull(dataSource);
    }

    @Test
    void testList() throws Exception {
        String tenantId = UUID.randomUUID().toString();
        String orgCode =  UUID.randomUUID().toString();
        String appCode =  UUID.randomUUID().toString();

        Tenant tenant = tenantService.createTenant(tenantId, orgCode, appCode);

        mvc.perform(mvcReqHelper.setUpWithoutToken(post("/api/v1/internal/tenants")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(tenant.getId().toString())))
                .andExpect(jsonPath("$[0].tenantId", is(tenant.getTenantId())))
                .andExpect(jsonPath("$[0].url", is(tenant.getUrl())))
                .andExpect(jsonPath("$[0].appCode", is(tenant.getAppCode())))
                .andExpect(jsonPath("$[0].orgCode", is(tenant.getOrgCode())))
                .andExpect(jsonPath("$[0].username", is(tenant.getUsername())))
                .andExpect(jsonPath("$[0].password", is(tenant.getPassword())))
                .andExpect(jsonPath("$[0].driverClassName", is(tenant.getDriverClassName())));

        tenantId = UUID.randomUUID().toString();
        orgCode =  UUID.randomUUID().toString();
        appCode =  UUID.randomUUID().toString();

        tenant = tenantService.createTenant(tenantId, orgCode, appCode);

        mvc.perform(mvcReqHelper.setUpWithoutToken(post("/api/v1/internal/tenants?appCode=" + appCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(tenant.getId().toString())))
                .andExpect(jsonPath("$[0].tenantId", is(tenant.getTenantId())))
                .andExpect(jsonPath("$[0].url", is(tenant.getUrl())))
                .andExpect(jsonPath("$[0].appCode", is(tenant.getAppCode())))
                .andExpect(jsonPath("$[0].orgCode", is(tenant.getOrgCode())))
                .andExpect(jsonPath("$[0].username", is(tenant.getUsername())))
                .andExpect(jsonPath("$[0].password", is(tenant.getPassword())))
                .andExpect(jsonPath("$[0].driverClassName", is(tenant.getDriverClassName())));
    }
}
