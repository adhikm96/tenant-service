package com.thebizio.biziotenantbaseservice.repo;

import com.thebizio.biziotenantbaseservice.entity.Tenant;
import com.thebizio.biziotenantbaseservice.projection.TenantProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepo extends JpaRepository<Tenant, UUID> {

    @Query("select tenant.id as id, tenant.tenantId as tenantId, tenant.orgCode as orgCode, tenant.appCode as appCode," +
            " tenant.url as url, tenant.username as username, tenant.password as password, tenant.driverClassName as driverClassName" +
            " from Tenant tenant order by tenant.modified desc")
    List<TenantProjection> fetchAll();

    boolean existsByTenantId(String tenantId);

    @Query("select tenant.id as id, tenant.tenantId as tenantId, tenant.orgCode as orgCode, tenant.appCode as appCode," +
            " tenant.url as url, tenant.username as username, tenant.password as password, tenant.driverClassName as driverClassName" +
            " from Tenant tenant where tenant.appCode = :appCode order by tenant.modified desc")
    List<TenantProjection> fetchAllByAppCode(Optional<String> appCode);
}
