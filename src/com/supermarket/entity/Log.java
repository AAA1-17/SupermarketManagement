package com.supermarket.entity;

import com.supermarket.enums.LogType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志实体
 */
public class Log {

    /** 日志编号（系统自动生成） */
    private final String id;
    /** 日志概要 */
    private final String title;
    /** 日志详情 */
    private final String description;
    /** 记录时间 */
    private final Date date;
    /** 是否成功 */
    private final boolean success;
    /** 日志类型 */
    private final LogType logType;

    public Log(String id, String title, String description, Date date,
               boolean success, LogType logType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.success = success;
        this.logType = logType;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public boolean isSuccess() {
        return success;
    }

    public LogType getLogType() {
        return logType;
    }

    @Override
    public String toString() {
        String time = date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        String type = logType == null ? "" : logType.getLabel();
        return String.format("%-8s %-19s %-10s %-4s %-18s %s",
                id, time, type, success ? "成功" : "失败", title, description);
    }
}
