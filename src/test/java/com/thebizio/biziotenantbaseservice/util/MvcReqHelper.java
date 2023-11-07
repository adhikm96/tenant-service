package com.thebizio.biziotenantbaseservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebizio.biziotenantbaseservice.testcontaines.BaseTestContainer;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class MvcReqHelper {
    public static String DEFAULT_PASSWORD = "password";

    @Autowired
    private ObjectMapper objectMapper;

    String getToken() {
        return "Bearer " + BaseTestContainer.keycloakAdminClient.tokenManager().getAccessTokenString();
    }

    public MockHttpServletRequestBuilder setUp(MockHttpServletRequestBuilder builder) {
        return builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .header("Authorization", getToken());
    }

    public MockHttpServletRequestBuilder setUp(MockHttpServletRequestBuilder builder, Object body)
            throws JsonProcessingException {
        return builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .header("Authorization", getToken());
    }

    public MockHttpServletRequestBuilder setUpWithoutToken(MockHttpServletRequestBuilder builder) {
        return builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder setUpWithoutToken(MockHttpServletRequestBuilder builder, Object body)
            throws JsonProcessingException {
        return builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));
    }

    public MockHttpServletRequestBuilder setUpWithoutToken(MockHttpServletRequestBuilder builder, Object body, String sigHeader)
            throws JsonProcessingException {
        return builder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .header("stripe-signature",sigHeader);
    }
}
