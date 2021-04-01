package it.jump3.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.security.TokenProvider;
import it.jump3.security.annotation.Secured;
import it.jump3.security.context.DecodedJWTSecurityContext;
import it.jump3.security.context.EmptySecurityContext;
import it.jump3.util.EnvironmentConstants;
import it.jump3.util.TokenUtils;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter extends CommonFilter implements ContainerRequestFilter {

    @Inject
    TokenProvider tokenProvider;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);


        if (authorizationHeader != null && authorizationHeader.startsWith(EnvironmentConstants.AUTHORIZATION_HEADER_PREFIX)) {

            String token = TokenUtils.getToken(authorizationHeader);

            try {

                DecodedJWT decodedJWT = tokenProvider.verify(token);
                containerRequestContext.setSecurityContext(new DecodedJWTSecurityContext(decodedJWT, tokenProvider));

            } catch (JWTVerificationException ex) {
                containerRequestContext.setSecurityContext(new EmptySecurityContext());
                throw new CommonBusinessException(Integer.toString(BusinessError.IB_401.code()), ex.getMessage(), Response.Status.UNAUTHORIZED);
                /*containerRequestContext.abortWith(
                        Response.ok(Response.Status.UNAUTHORIZED.toString())
                                .status(Response.Status.UNAUTHORIZED)
                                .build());*/
            }
        } else {

            containerRequestContext.setSecurityContext(new EmptySecurityContext());
            if (!isSecurityOptional()) {
                throw new CommonBusinessException(Integer.toString(BusinessError.IB_401.code()), "Unauthorized", Response.Status.UNAUTHORIZED);
                //throw new UnauthorizedException();
            }
        }
    }

    private boolean isSecurityOptional() {
        Class<?> resourceClass = resourceInfo.getResourceClass();
        Method resourceMethod = resourceInfo.getResourceMethod();
        return isSecurityOptional(resourceMethod, resourceClass);
    }
}
