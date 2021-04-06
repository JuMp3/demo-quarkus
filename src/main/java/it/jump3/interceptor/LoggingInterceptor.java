package it.jump3.interceptor;

import io.opentracing.Tracer;
import io.vertx.core.http.HttpServerRequest;
import it.jump3.annotation.Trace;
import it.jump3.config.ConfigService;
import it.jump3.log.LogBuilder;
import it.jump3.util.HeaderData;
import it.jump3.util.Utility;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpRequest;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.SecurityContext;
import java.io.Serializable;

@Trace
@Interceptor
@Priority(3000)
@Slf4j
public class LoggingInterceptor implements Serializable {

    private static final long serialVersionUID = 2567399247998247171L;

    @Inject
    Tracer tracer;

    @Inject
    ConfigService configService;

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {

        ContextInfo contextInfo = getContextInfo(context);
        long start = System.currentTimeMillis();
        HeaderData headerData = new HeaderData();

        if (contextInfo.getHttpRequest() != null && contextInfo.getHttpServerRequest() != null &&
                contextInfo.getSecurityContext() != null) {
            // ...log before
            headerData = Utility.headerData(contextInfo.getHttpRequest(), contextInfo.getHttpServerRequest(), contextInfo.getSecurityContext());
            LogBuilder.logStart(log, context, tracer, start, headerData, configService.getLogLevel());
        }

        Object ret = context.proceed();

        // ...log after
        LogBuilder.logEnd(log, context, tracer, start, headerData, ret, configService.getLogLevel());

        return ret;
    }

    private ContextInfo getContextInfo(InvocationContext context) {
        ContextInfo contextInfo = new ContextInfo();
        for (Object parameter : context.getParameters()) {
            if (parameter instanceof SecurityContext) {
                contextInfo.setSecurityContext((SecurityContext) parameter);
            } else if (parameter instanceof HttpRequest) {
                contextInfo.setHttpRequest((HttpRequest) parameter);
            } else if (parameter instanceof HttpServerRequest) {
                contextInfo.setHttpServerRequest((HttpServerRequest) parameter);
            }
        }
        return contextInfo;
    }

    @Data
    @NoArgsConstructor
    private static class ContextInfo {
        private SecurityContext securityContext;
        private HttpRequest httpRequest;
        private HttpServerRequest httpServerRequest;
    }
}
