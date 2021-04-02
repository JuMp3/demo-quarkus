package it.jump3.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Tracer;
import it.jump3.annotation.RequestParamObject;
import it.jump3.exception.ErrorResponse;
import it.jump3.file.FilesResponse;
import it.jump3.util.DateUtil;
import it.jump3.util.HeaderData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.slf4j.Logger;

import javax.interceptor.InvocationContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogBuilder extends LogBuilderCommon {

    public static void logStart(Logger log, InvocationContext invocationContext, Tracer tracer, long start,
                                HeaderData headerData, String logLevel) throws JsonProcessingException {

        //CHECK IF RESPONSE IS RESPONSEENTITY TYPE
        String requestBody = "\"Request-body\":";
        String requestParams = "\"Request-params\":";
        String pathParams = "\"Path-params\":";
        String header, requestParamObject = null;

        Method method = invocationContext.getMethod();

        List<Integer> requestParamPositions = new ArrayList<>();
        Map<String, Object> requestParamValues = new HashMap<>();

        List<Integer> pathParamPositions = new ArrayList<>();
        Map<String, Object> pathParamValues = new HashMap<>();

        int requestBodyIdx = -1;
        int requestParamObjIdx = -1;
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof RequestBody) {
                    requestBodyIdx = i;
                } else if (a instanceof RequestParamObject) {
                    requestParamObjIdx = i;
                } else if (a instanceof QueryParam) {
                    requestParamPositions.add(i);
                } else if (a instanceof PathParam) {
                    pathParamPositions.add(i);
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if (requestBodyIdx != -1) {
            requestBody = requestBody.concat(objectMapper.writeValueAsString(invocationContext.getParameters()[requestBodyIdx]));
        } else {
            requestBody = requestBody.concat("{}");
        }

        if (requestParamObjIdx != -1) {
            requestParamObject = objectMapper.writeValueAsString(invocationContext.getParameters()[requestParamObjIdx]);
        }

        if (!CollectionUtils.isEmpty(requestParamPositions)) {
            requestParamPositions.forEach(idx -> requestParamValues.put(method.getParameterTypes()[idx].getName(), invocationContext.getParameters()[idx]));
            requestParams = requestParams.concat(objectMapper.writeValueAsString(requestParamValues));
        } else if (!ObjectUtils.isEmpty(requestParamObject)) {
            requestParams = requestParams.concat(requestParamObject);
        } else {
            requestParams = requestParams.concat("{}");
        }

        if (!CollectionUtils.isEmpty(pathParamPositions)) {
            pathParamPositions.forEach(idx -> pathParamValues.put(method.getParameterTypes()[idx].getName(), invocationContext.getParameters()[idx]));
            pathParams = pathParams.concat(objectMapper.writeValueAsString(pathParamValues));
        } else {
            pathParams = pathParams.concat("{}");
        }

        String body = "{".concat(requestParams).concat(", ").concat(pathParams).concat(", ").concat(requestBody).concat("}");

        if (headerData != null) {
            header = headerData.toString();
        } else {
            header = "{}";
        }

        CustomLog customLog = produceStartLog(invocationContext,
                tracer,
                start,
                logLevel,
                headerData.getUsername(),
                body,
                header);

        log.info(customLog.toString());
    }

    public static void logEnd(Logger log, InvocationContext invocationContext, Tracer tracer, long start,
                              HeaderData headerData, Object result, String logLevel) throws JsonProcessingException {

        Response restResponse = null;
        String body;

        if (result instanceof Response) {
            restResponse = (Response) result;

            if (restResponse.getEntity() instanceof byte[]) {
                body = "\"file\"";
            } else if (restResponse.getEntity() instanceof FilesResponse) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\"files\":[");
                FilesResponse filesResponse = restResponse.readEntity(FilesResponse.class);
                if (!CollectionUtils.isEmpty(filesResponse.getFiles())) {
                    sb.append(filesResponse.getFiles().stream()
                            .filter(file -> file != null && !ObjectUtils.isEmpty(file.getFileName()))
                            .map(file -> "\"".concat(file.getFileName()).concat("\""))
                            .collect(Collectors.joining(",")));
                }
                sb.append("]}");
                body = sb.toString();
            } else {
                body = new ObjectMapper().writeValueAsString(result);
            }
        } else {
            body = new ObjectMapper().writeValueAsString(result);
        }

        CustomLog customLog = produceEndLog(invocationContext,
                tracer,
                start,
                logLevel,
                headerData.getUsername(),
                body,
                restResponse);

        log.info(customLog.toString());
    }

    public static LoggingBuilder populateBuilder(LoggingBuilder builder, InvocationContext invocationContext, long start, String logLevel,
                                                 String username, String body) {

        //BUILD LOGGING DTO WITH CALL INFOS
        return builder.setDateTime(DateUtil.nowLocalDateTimeItaly())
                .setTime(System.currentTimeMillis() - start)
                .setUsername(username)
                .setLoggerName(invocationContext.getMethod().getDeclaringClass().getName())
                .setThreadName(Thread.currentThread().getName())
                .setProcessName(invocationContext.getMethod().getName())
                .setLogLevel(logLevel)
                .setPid(String.valueOf(ProcessHandle.current().pid())) // FOR JAVA 9
                //.setPid(String.valueOf(getPID()))
                .setBody(body);
    }

    private static CustomLog produceStartLog(InvocationContext invocationContext, Tracer tracer, long start, String logLevel,
                                             String username, String body, String header) {

        LoggingBuilder builder = initLoggingBuilder(tracer);
        populateBuilder(builder, invocationContext, start, logLevel, username, body);
        builder.setHeaderData(header);

        return builder.build();
    }

    private static CustomLog produceEndLog(InvocationContext invocationContext, Tracer tracer, long start, String logLevel,
                                           String username, String body, Response restResponse) {

        LoggingBuilder builder = initLoggingBuilder(tracer);
        populateBuilder(builder, invocationContext, start, logLevel, username, body);
        builder.setResultCode(restResponse != null ? restResponse.getStatus() : null)
                .setErrorCode(restResponse != null && restResponse.getEntity() != null && restResponse.getEntity() instanceof ErrorResponse ? Integer.toString(restResponse.getStatus()) : null)
                .setErrorMessage(restResponse != null && restResponse.getEntity() != null && restResponse.getEntity() instanceof ErrorResponse ? restResponse.readEntity(ErrorResponse.class).getMessage() : null);

        return builder.build();
    }

    public static void logExceptionDTO(Logger log, ResourceInfo resourceInfo, Tracer tracer,
                                       HeaderData headerData, String logLevel, Response response) {

        LoggingBuilder builder = initLoggingBuilder(tracer);

        //BUILD LOGGING DTO WITH CALL INFOS
        CustomLog customLog = builder.setDateTime(DateUtil.nowLocalDateTimeItaly())
                .setUsername(headerData.getUsername())
                .setThreadName(Thread.currentThread().getName())
                .setProcessName(resourceInfo.getResourceMethod().getName())
                .setLoggerName(resourceInfo.getResourceMethod().getDeclaringClass().getName())
                .setLogLevel(logLevel)
                .setPid(String.valueOf(ProcessHandle.current().pid())) // FOR JAVA 9
                //.setPid(String.valueOf(getPID()))
                .setResultCode(response != null ? response.getStatus() : null)
                .setErrorCode(response != null && response.getEntity() != null && response.getEntity() instanceof ErrorResponse ? Integer.toString(response.getStatus()) : null)
                .setErrorMessage(response != null && response.getEntity() != null && response.getEntity() instanceof ErrorResponse ? response.readEntity(ErrorResponse.class).getMessage() : null)
                //BUILD THE LOGGING DTO AND LOG IT
                .build();

        log.info(customLog.toString());
    }
}
