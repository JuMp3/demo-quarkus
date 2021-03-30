package it.jump3.exception;

import io.opentracing.Tracer;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Inject
    Tracer tracer;

    @Override
    public Response toResponse(Exception e) {
        ErrorResponse errorResponse = getResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(errorResponse).build();
    }

    public ErrorResponse getResponse(int code, Exception ex) {
        return ErrorUtils.getResponse(tracer.activeSpan(), code, ex);
    }

    public ErrorResponse getResponse(int code, List<String> msgs) {
        return ErrorUtils.getResponse(tracer.activeSpan(), code, msgs);
    }
}
