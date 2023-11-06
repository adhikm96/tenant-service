package com.thebizio.biziotenantbaseservice.projection;

public interface DSPrj {
    String getId();

    String getTenantId();

    String getUrl();
    String getAppCode();
    String getOrgCode();

    String getUsername();

    String getPassword();

    String getDriverClassName();
}
