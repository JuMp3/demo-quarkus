package it.jump3.exception;

import io.opentracing.Tracer;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CommonBusinessExceptionHandler implements ExceptionMapper<CommonBusinessException> {

    @Inject
    Tracer tracer;

    @Override
    public Response toResponse(CommonBusinessException e) {
        ErrorResponse errorResponse = ErrorUtils.getBusinessErrorResponse(tracer.activeSpan(), e);
        return Response.status(e.getHttpStatus()).entity(errorResponse).build();
    }
}
