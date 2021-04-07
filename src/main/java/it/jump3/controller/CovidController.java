package it.jump3.controller;

import it.jump3.rest.CovidClient;
import it.jump3.rest.model.CovidResponse;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/covid")
@Tag(name = "covid")
@Slf4j
public class CovidController {

    @Inject
    @RestClient
    CovidClient covidClient;

    @GET
    @Path("/summary")
    public Response summary() {

        log.info("**** START -> summary ****");
        CovidResponse response = covidClient.getSummary();
        log.info("**** END -> summary ****");

        return Response.ok(response).build();
    }
}
