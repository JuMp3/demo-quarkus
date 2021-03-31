package it.jump3.controller;

import io.vertx.core.http.HttpServerRequest;
import it.jump3.enumz.EnvironmentConstants;
import it.jump3.util.HeaderData;
import it.jump3.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.spi.HttpRequest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

@Path("/config")
@Tag(name = "config")
@Slf4j
public class ConfigResource {

    @ConfigProperty(name = "constant.speed-of-sound-in-meter-per-second", defaultValue = "343")
    int speedOfSound;

    @ConfigProperty(name = "display.mach")
    Optional<Integer> displayMach;

    @ConfigProperty(name = "display.unit.name")
    String displayUnitName;

    @ConfigProperty(name = "display.unit.factor")
    BigDecimal displayUnitFactor;

    @Context
    HttpServerRequest request;

    @GET
    @Path("supersonic")
    @Produces(MediaType.TEXT_PLAIN)
    public String supersonic() {
        final int mach = displayMach.orElse(1);
        final BigDecimal speed = BigDecimal.valueOf(speedOfSound)
            .multiply(displayUnitFactor)
            .multiply(BigDecimal.valueOf(mach));
        return String.format("Mach %d is %.3f %s",
            mach,
            speed,
            displayUnitName
        );
    }

    @GET
    @Path("ip")
    @Produces(MediaType.APPLICATION_JSON)
    public HeaderData ip(@Context HttpRequest req) {
        return Utility.headerData(req, request);
    }

    @GET
    @Path("lang")
    @Produces(MediaType.APPLICATION_JSON)
    public Locale ip(@Context HttpServerRequest request) {
        return Utility.stringToLocale(request.getHeader("Accept-language"));
    }
}