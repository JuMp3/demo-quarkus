package it.jump3.exception;

import io.opentracing.Tracer;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Inject
    Tracer tracer;

    @Override
    public Response toResponse(Exception e) {
        ErrorResponse errorResponse = ErrorUtils.getResponse(tracer.activeSpan(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(errorResponse).build();
    }
}
