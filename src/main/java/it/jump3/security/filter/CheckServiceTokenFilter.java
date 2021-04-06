package it.jump3.security.filter;

import it.jump3.config.ConfigService;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.security.annotation.CheckServiceToken;
import it.jump3.util.TokenUtils;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@CheckServiceToken
@Provider
@Priority(Priorities.AUTHENTICATION + 1)
public class CheckServiceTokenFilter implements ContainerRequestFilter {

    @Inject
    ConfigService configService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String token = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!this.configService.getServiceToken().equals(TokenUtils.getToken(token))) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_401_WRONG_TOKEN.code()),
                    "Wrong token", Response.Status.UNAUTHORIZED);
        }
    }
}
