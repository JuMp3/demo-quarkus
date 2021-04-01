package it.jump3.security.filter;

import it.jump3.security.annotation.Secured;

import java.lang.reflect.Method;

public class CommonFilter {

    protected boolean isSecurityOptional(Method resourceMethod, Class<?> resourceClass) {

        Secured resourceClassSecured = resourceClass.getAnnotation(Secured.class);
        Secured resourceMethodSecured = resourceMethod.getAnnotation(Secured.class);

        if (resourceMethodSecured != null) {
            return resourceMethodSecured.optional();
        }

        if (resourceClassSecured != null) {
            return resourceClassSecured.optional();
        }

        return false;
    }
}
