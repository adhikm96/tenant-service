package com.thebizio.biziotenantbaseservice.projection;

public interface TenantProjection {
    String getId();

    String getTenantId();

    String getUrl();
    String getAppCode();
    String getOrgCode();

    String getUsername();

    String getPassword();

    String getDriverClassName();
}
