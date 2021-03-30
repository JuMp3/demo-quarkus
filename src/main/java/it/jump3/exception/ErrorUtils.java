package it.jump3.exception;

import io.opentracing.Span;
import it.jump3.enumz.BusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
public class ErrorUtils {

    public static ErrorResponse getResponse(Span span, int code, Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());

        setErrorResponse(span, code, errorResponse);

        return errorResponse;
    }

    public static ErrorResponse getResponse(Span span, int code, List<String> msgs) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessages(msgs);

        setErrorResponse(span, code, errorResponse);

        return errorResponse;
    }

    private static void setErrorResponse(Span span, int code, ErrorResponse errorResponse) {

        setCorrelationId(span, errorResponse);
        errorResponse.setHttpStatusCode(code);
        errorResponse.setErrorCode(BusinessError.fromCode(code).toString());

        if (!ObjectUtils.isEmpty(errorResponse.getMessage())) log.error(errorResponse.getMessage());
    }

    public static ErrorResponse getBusinessErrorResponse(Span span, CommonBusinessException commonBusinessException) {

        ErrorResponse errorResponse = new ErrorResponse();
        setCorrelationId(span, errorResponse);
        errorResponse.setHttpStatusCode(commonBusinessException.getHttpStatus().getStatusCode());
        errorResponse.setErrorCode(commonBusinessException.getErrorCode());
        if (!CollectionUtils.isEmpty(commonBusinessException.getErrorMessageList())) {
            errorResponse.setMessages(commonBusinessException.getErrorMessageList());
        } else {
            errorResponse.setMessage(commonBusinessException.getMessage());
        }

        if (!ObjectUtils.isEmpty(errorResponse.getMessage())) {
            log.error(errorResponse.getMessage());
        }

        return errorResponse;
    }

    private static void setCorrelationId(Span span, ErrorResponse errorResponse) {
        errorResponse.setCorrelationId(span != null && span.context() != null ?
                span.context().toString() :
                UUID.randomUUID().toString());
    }
}
