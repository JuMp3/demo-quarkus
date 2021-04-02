package it.jump3.log;

import io.opentracing.Tracer;
import it.jump3.util.DateUtil;
import it.jump3.util.HeaderData;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;

public class LogBuilderCommon {

    protected static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    protected static LoggingBuilder initLoggingBuilder(Tracer tracer) {

        //CHECK FOR ZIPKIN SPAN, IF TRACE IS NOT PRESENT IT MEANS THIS IS THE FIRST CALL THUS TRACE IS EQUAL SPAN
        LoggingBuilder builder = new LoggingBuilder();
        if (tracer != null && tracer.activeSpan() != null) {
            builder//.setBid(StringUtils.isEmpty(tracer.toString()) ? tracer.activeSpan().context().toString() : tracer.toString())
                    .setSid(tracer.activeSpan().context().toString());
        }

        return builder;
    }

    public static void logDTORequest(Logger log, Tracer tracer, long start, HeaderData headerData, String extraData, String logLevel) {

        LoggingBuilder builder = initLoggingBuilder(tracer);

        //BUILD LOGGING DTO WITH CALL INFOS
        CustomLog customLog = builder.setDateTime(DateUtil.nowLocalDateTimeItaly())
                .setTime(System.currentTimeMillis() - start)
                .setUsername(headerData.getUsername())
                .setThreadName(Thread.currentThread().getName())
                .setLogLevel(logLevel)
                .setPid(String.valueOf(ProcessHandle.current().pid())) // FOR JAVA 9
                //.setPid(String.valueOf(getPID()))
                .setBody(extraData)
                .build();

        log.info(customLog.toString());
    }

    public static void logDTOResponse(Logger log, Tracer tracer, long start, HeaderData headerData, String extraData,
                                      Integer resultCode, String errorCode, String errorMessage, String logLevel) {

        LoggingBuilder builder = initLoggingBuilder(tracer);

        //BUILD LOGGING DTO WITH CALL INFOS
        CustomLog customLog = builder.setDateTime(DateUtil.nowLocalDateTimeItaly())
                .setTime(System.currentTimeMillis() - start)
                //.setUsername(headerData.getUsername())
                .setThreadName(Thread.currentThread().getName())
                .setLogLevel(logLevel)
                .setPid(String.valueOf(ProcessHandle.current().pid())) // FOR JAVA 9
//                .setPid(String.valueOf(getPID()))
                .setBody(extraData)
                //THESE PARAMS ARE FILLED IN CASE OF REST CALL OR REST CALL ERROR (ERRORRESPONSE)
                .setResultCode(resultCode)
                .setErrorCode(errorCode)
                .setErrorMessage(errorMessage)
                .build();

        log.info(customLog.toString());
    }

    public static void logSoapDTO(Logger logger, String messageBody, String logLevel, boolean isRequest) {

        LoggingBuilder builder = new LoggingBuilder();
        //CHECK FOR ZIPKIN SPAN, IF TRACE IS NOT PRESENT IT MEANS THIS IS THE FIRST CALL THUS TRACE IS EQUAL SPAN
        //BUILD LOGGING DTO WITH CALL INFOS

        CustomLog loggingDTO = builder.setDateTime(DateUtil.nowLocalDateTimeItaly())
                .setThreadName(Thread.currentThread().getName())
                .setLogLevel(logLevel)
                .setPid(String.valueOf(ProcessHandle.current().pid())) // FOR JAVA 9
                //.setPid(String.valueOf(getPID()))
                .setResultCode(!isRequest ? Response.Status.OK.getStatusCode() : null)
                .setBody(messageBody)
                //BUILD THE LOGGINGDTO AND LOG IT
                .build();

        logger.info(loggingDTO.toString());
    }


}
