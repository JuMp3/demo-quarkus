package it.jump3.controller;

import it.jump3.controller.model.GenericResponse;
import it.jump3.service.BitlyService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bilty")
@Tag(name = "bilty")
@Slf4j
public class BitlyController {

    @Inject
    BitlyService bitlyService;

    @GET
    @Path("/shorten")
    @Produces(MediaType.APPLICATION_JSON)
    public Response shorten(@QueryParam("url") @Valid @NotBlank(message = "URL may not be blank") String url) {

        log.info("**** START -> shorten ****");
        String shortUrl = bitlyService.getShortUrl(url);
        log.info("**** END -> shorten ****");

        return Response.ok(new GenericResponse(shortUrl)).build();
    }
}
