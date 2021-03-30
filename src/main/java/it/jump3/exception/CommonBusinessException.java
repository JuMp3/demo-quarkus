package it.jump3.exception;

import it.jump3.util.Utility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class CommonBusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -5437331780618775324L;

    private String errorCode;
    private List<String> errorMessageList;
    private Response.Status httpStatus;

    public CommonBusinessException(String errorCode, String errorMessage, Response.Status httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessageList = Collections.singletonList(errorMessage);
        this.httpStatus = httpStatus;
    }

    public CommonBusinessException(String errorCode, List<String> errorMessageList, Response.Status httpStatus) {
        super(!CollectionUtils.isEmpty(errorMessageList) ? Utility.joinListWithChar(errorMessageList, ", ") : null);
        this.errorCode = errorCode;
        this.errorMessageList = errorMessageList;
        this.httpStatus = httpStatus;
    }

    public CommonBusinessException(String errorCode, Set<? extends ConstraintViolation<?>> violations, Response.Status httpStatus) {
        super(violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", ")));
        this.errorCode = errorCode;
        this.errorMessageList = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        this.httpStatus = httpStatus;
    }
}
