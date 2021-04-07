package it.jump3.rest;

import it.jump3.rest.model.CovidResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

//@Path("/v2")
@Singleton
@RegisterRestClient(configKey = "covid-api")
public interface CovidClient {

    @GET
    @Path("/summary")
    @Produces(MediaType.APPLICATION_JSON)
    CovidResponse getSummary();

    @GET
    @Path("/summary")
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<CovidResponse> getSummaryAsync();
}
