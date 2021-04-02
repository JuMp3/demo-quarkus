package it.jump3.exception;

import io.opentracing.Tracer;
import io.vertx.core.http.HttpServerRequest;
import it.jump3.log.LogBuilder;
import it.jump3.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.spi.HttpRequest;

import javax.inject.Inject;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class CommonBusinessExceptionHandler implements ExceptionMapper<CommonBusinessException> {

    @Inject
    Tracer tracer;

    @Context
    SecurityContext securityContext;

    @Context
    HttpRequest httpRequest;

    @Context
    HttpServerRequest httpServerRequest;

    @Context
    private ResourceInfo resourceInfo;

    @ConfigProperty(name = "quarkus.log.level")
    String logLevel;

    @Override
    public Response toResponse(CommonBusinessException e) {

        ErrorResponse errorResponse = ErrorUtils.getBusinessErrorResponse(tracer.activeSpan(), e);
        Response response = Response.status(e.getHttpStatus()).entity(errorResponse).build();
        LogBuilder.logExceptionDTO(log, resourceInfo, tracer,
                Utility.headerData(httpRequest, httpServerRequest, securityContext),
                logLevel, response);

        return response;
    }
}
