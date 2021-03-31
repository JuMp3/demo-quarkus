package it.jump3.exception;

import io.opentracing.Tracer;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    Tracer tracer;

    @Override
    public Response toResponse(ConstraintViolationException e) {

        List<String> msgList = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorUtils.getResponse(tracer.activeSpan(), Response.Status.BAD_REQUEST.getStatusCode(), msgList);
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }
}
