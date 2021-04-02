package it.jump3.annotation;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
}
