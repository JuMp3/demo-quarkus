package it.jump3.exception;

import it.jump3.enumz.BusinessError;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

//@Provider
public class RestExceptionHandler implements ResponseExceptionMapper<CommonBusinessException> {

    @Override
    public CommonBusinessException toThrowable(Response response) {
        return new CommonBusinessException(Integer.toString(BusinessError.IB_500_REST_CLIENT.code()),
                response.readEntity(BitlyErrorResponse.class).getDescription(),
                Response.Status.fromStatusCode(response.getStatus()));
    }
}
