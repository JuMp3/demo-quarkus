package it.jump3.log;

import it.jump3.util.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

@Data
public class CustomLog {

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

    public CustomLog(LocalDateTime dateTime, String logLevel, String sid, String bid,
                     String username, String pid, String threadName, String processName, String loggerName,
                     Integer resultCode, String errorCode, String errorMessage, Long time, String body, String headerData) {

        this.dateTime = dateTime;
        this.logLevel = logLevel;
        this.sid = sid;
        this.bid = bid;
        this.username = username;
        this.pid = pid;
        this.threadName = threadName;
        this.processName = processName;
        this.loggerName = loggerName;
        this.resultCode = resultCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.time = time;
        this.body = body;
        this.headerData = headerData;
    }

    @Override
    public String toString() {
        return "{"
                + "\"dateTime\":\"" + (dateTime != null ? DateUtil.getFromLocalDateTimeDefault(dateTime) : "none") + "\""
                + ", \"logLevel\":\"" + logLevel + "\""
                + ", \"sid\":\"" + sid + "\""
                + ", \"bid\":\"" + bid + "\""
                + ", \"username\":\"" + username + "\""
                + ", \"pid\":\"" + pid + "\""
                + ", \"threadName\":\"" + threadName + "\""
                + ", \"processName\":\"" + processName + "\""
                + ", \"loggerName\":\"" + loggerName + "\""
                + (resultCode == null ? "" : ", \"resultCode\":\"" + resultCode + "\"")
                + (ObjectUtils.isEmpty(errorCode) ? "" : ", \"errorCode\":\"" + errorCode + "\"")
                + (ObjectUtils.isEmpty(errorMessage) ? "" : ", \"errorMessage\":\"" + errorMessage + "\"")
                + ", \"time\":\"" + time + "\""
                + ", \"body\":" + (ObjectUtils.isEmpty(body) ? "{}" : body)
                + (ObjectUtils.isEmpty(headerData) ? "" : ", \"headerData\":\"" + headerData + "\"")
                + "}";
    }
}
