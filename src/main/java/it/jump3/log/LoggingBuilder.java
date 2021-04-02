package it.jump3.log;

import java.time.LocalDateTime;

public class LoggingBuilder {

    private LocalDateTime dateTime;
    private String logLevel;
    private String sid;
    private String bid;
    private String username;
    private String pid;
    private String threadName;
    private String processName;
    private String loggerName;
    private Integer resultCode;
    private String errorCode;
    private String errorMessage;
    private Long time;
    private String body;
    private String headerData;

    public LoggingBuilder setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public LoggingBuilder setLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public LoggingBuilder setSid(String sid) {
        this.sid = sid;
        return this;
    }

    public LoggingBuilder setBid(String bid) {
        this.bid = bid;
        return this;
    }

    public LoggingBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoggingBuilder setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public LoggingBuilder setThreadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public LoggingBuilder setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    public LoggingBuilder setLoggerName(String loggerName) {
        this.loggerName = loggerName;
        return this;
    }

    public LoggingBuilder setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public LoggingBuilder setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public LoggingBuilder setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public LoggingBuilder setTime(Long time) {
        this.time = time;
        return this;
    }

    public LoggingBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public LoggingBuilder setHeaderData(String headerData) {
        this.headerData = headerData;
        return this;
    }

    public CustomLog build() {
        return new CustomLog(dateTime, logLevel, sid, bid, username, pid, threadName, processName, loggerName,
                resultCode, errorCode, errorMessage, time, body, headerData);
    }
}
