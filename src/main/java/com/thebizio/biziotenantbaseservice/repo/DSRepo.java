package com.thebizio.biziotenantbaseservice.repo;

import com.thebizio.biziotenantbaseservice.entity.DataSource;
import com.thebizio.biziotenantbaseservice.projection.DSPrj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DSRepo extends JpaRepository<DataSource, UUID> {

    @Query("select ds.id as id, ds.tenantId as tenantId, ds.orgCode as orgCode, ds.appCode as appCode," +
            " ds.url as url, ds.username as username, ds.password as password, ds.driverClassName as driverClassName" +
            " from DataSource ds")
    List<DSPrj> fetchAll();

    boolean existsByTenantId(String tenantId);
}
