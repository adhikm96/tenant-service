package com.thebizio.biziotenantbaseservice.testcontaines;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Random;

public class BaseTestContainer {

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("bz-tenant-service" + new Random().nextInt(100000));

    public static KeycloakContainer keycloak = new KeycloakContainer("jboss/keycloak")
            .withEnv("DB_VENDOR", "h2");

    public static Keycloak keycloakAdminClient = null;

//    public static String KEYCLOAK_REALM = "test-realm";
    public static String KEYCLOAK_REALM = "master";
    public static String KEYCLOAK_RESOURCE = "test-client";

    static {
        keycloak.start();
        postgres.start();

        keycloakAdminClient = KeycloakBuilder.builder()
                .serverUrl(keycloak.getAuthServerUrl())
                .realm("master")
                .clientId("admin-cli")
                .username(keycloak.getAdminUsername())
                .password(keycloak.getAdminPassword())
                .build();

//        RealmRepresentation realmRepresentation = new RealmRepresentation();
//        realmRepresentation.setRealm(KEYCLOAK_REALM);
//        realmRepresentation.setEnabled(true);
//        realmRepresentation.setId(KEYCLOAK_REALM);
//        keycloakAdminClient.realms().create(realmRepresentation);

        testClientCreation();
    }

    private static void testClientCreation() {
        // creating a client in master for later user
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(KEYCLOAK_RESOURCE);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setEnabled(true);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setProtocol("openid-connect");
        if(clientRepresentation.getAttributes() == null) {
            clientRepresentation.setAttributes(new HashMap<>());
        }
//        clientRepresentation.getAttributes().put("access.token.lifespan", String.valueOf(3600 * 12)); // 12 hrs

        Response res = keycloakAdminClient.realm(KEYCLOAK_REALM).clients().create(clientRepresentation);
        if(res.getStatus() >= 400) System.out.println(res.readEntity(ErrorRepresentation.class).getErrorMessage());
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("keycloak.realm", () -> KEYCLOAK_REALM);
        registry.add("keycloak.auth-server-url", keycloak::getAuthServerUrl);
        registry.add("keycloak.resource",() -> KEYCLOAK_RESOURCE);
    }
}
