package it.jump3.security.filter;

import io.quarkus.security.ForbiddenException;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.security.annotation.Secured;
import it.jump3.security.profile.Role;
import it.jump3.user.UserInfo;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter extends CommonFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        Class<?> resourceClass = resourceInfo.getResourceClass();
        Method resourceMethod = resourceInfo.getResourceMethod();

        if (!isSecurityOptional(resourceMethod, resourceClass)) {

            SecurityContext securityContext = containerRequestContext.getSecurityContext();

            List<Role> classRoles = extractRoles(resourceClass);
            List<Role> methodRoles = extractRoles(resourceMethod);

            try {
                if (methodRoles.isEmpty()) {
                    checkPermissions(classRoles, securityContext);
                } else {
                    checkPermissions(methodRoles, securityContext);
                }
            } catch (ForbiddenException ex) {
                //containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                throw new CommonBusinessException(Integer.toString(BusinessError.IB_403.code()),
                        String.format("User %s forbidden for this functionality", ((UserInfo) securityContext.getUserPrincipal()).getUsername()), Response.Status.FORBIDDEN);
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles, SecurityContext securityContext) {
        if (allowedRoles.contains(Role.EVERYONE)) return; // EVERYONE bypass this check
        if (!isAccessAllowed(allowedRoles, securityContext)) {
            throw new ForbiddenException();
        }
    }

    private boolean isAccessAllowed(List<Role> allowedRoles, SecurityContext securityContext) {
        for (Role allowedRole : allowedRoles) {
            if (securityContext.isUserInRole(allowedRole.name())) {
                return true;
            }
        }
        return false;
    }

    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new LinkedList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new LinkedList<>();
            } else {
                return Arrays.asList(secured.value());
            }
        }
    }
}
