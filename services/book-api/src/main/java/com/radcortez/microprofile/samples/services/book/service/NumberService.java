package com.radcortez.microprofile.samples.services.book.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

@ApplicationScoped
public class NumberService {
    @Inject
    @ConfigProperty(name = "NUMBER_TARGET_URL", defaultValue = "http://localhost:8081/number-api/numbers/generate")
    private String numberApiTargetUrl;

    private Client client;
    private WebTarget numberApi;

    @PostConstruct
    private void init() {
        client = ClientBuilder.newClient();
        numberApi = client.target(numberApiTargetUrl);
    }

    @PreDestroy
    private void destroy() {
        client.close();
    }

    public String getNumber() {
        final Response response = numberApi.request().get();

        if (OK.getStatusCode() == response.getStatus()) {
            return response.readEntity(String.class);
        }

        throw new WebApplicationException(INTERNAL_SERVER_ERROR);
    }
}