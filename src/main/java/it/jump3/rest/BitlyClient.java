package it.jump3.rest;

import it.jump3.exception.RestExceptionHandler;
import it.jump3.rest.model.BitlyRequest;
import it.jump3.rest.model.BitlyResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Path("/v4")
@RegisterProvider(RestExceptionHandler.class)
@Singleton
@RegisterRestClient(configKey = "bitly-api")
public interface BitlyClient {

    @POST
    @Path("/shorten")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    BitlyResponse shorten(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, BitlyRequest request);

    @POST
    @Path("/shorten")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    CompletionStage<BitlyResponse> shortenAsync(@HeaderParam(HttpHeaders.AUTHORIZATION) String token, BitlyRequest request);
}
