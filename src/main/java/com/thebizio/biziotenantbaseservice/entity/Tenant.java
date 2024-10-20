package com.thebizio.biziotenantbaseservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
public class Tenant extends LastUpdateDetail{
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column
    private UUID id;

    private String tenantId;
    private String orgCode;
    private String appCode;

    private String url;

    private String username;

    private String password;

    private String driverClassName;
}
