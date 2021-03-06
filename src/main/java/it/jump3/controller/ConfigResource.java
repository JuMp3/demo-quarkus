package it.jump3.controller;

import io.vertx.core.http.HttpServerRequest;
import it.jump3.config.ConfigService;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

@Path("/config")
@Tag(name = "config")
@Slf4j
public class ConfigResource {

    @Inject
    ConfigService configService;

    @Context
    HttpServerRequest request;

//    @Inject
//    HeaderData headerData;

    @GET
    @Path("supersonic")
    @Produces(MediaType.TEXT_PLAIN)
    public String supersonic() {
        final int mach = configService.getDisplayMach().orElse(1);
        final BigDecimal speed = BigDecimal.valueOf(configService.getSpeedOfSound())
                .multiply(configService.getDisplayUnitFactor())
                .multiply(BigDecimal.valueOf(mach));
        return String.format("Mach %d is %.3f %s",
                mach,
                speed,
                configService.getDisplayUnitName()
        );
    }

    @GET
    @Path("header-data")
    @Produces(MediaType.APPLICATION_JSON)
    public HeaderData headerData(@Context HttpRequest req, @Context SecurityContext securityContext) {
        return Utility.headerData(req, request, securityContext);
    }

    @GET
    @Path("lang")
    @Produces(MediaType.APPLICATION_JSON)
    public Locale lang() {
        return Utility.stringToLocale(request.getHeader("Accept-language"));
    }
}
