package com.supermarket.entity;

import com.supermarket.enums.LogType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // ==================== 测试用例 ====================

    /** 直接运行本方法可单独测试日志实体 */
    public static void main(String[] args) {
        List<Log> logs = new ArrayList<>();
        logs.add(new Log("L0001", "系统登录成功", "用户名：admin", new Date(), true, LogType.LOGIN));
        logs.add(new Log("L0002", "新增收银员", "新增收银员：端木玉（92930120027）", new Date(), true, LogType.CASHIER));
        logs.add(new Log("L0003", "新增商品", "新增商品：可乐（P0001）", new Date(), true, LogType.PRODUCT));
        logs.add(new Log("L0004", "新增订单失败", "库存不足：可乐，现有 0", new Date(), false, LogType.ORDER));
        logs.add(new Log("L0005", "移除订单", "移除订单：O0001", new Date(), true, LogType.ORDER));

        System.out.println("------------------- 日志列表 -------------------");
        System.out.printf("%-8s %-19s %-10s %-4s %-18s %s%n",
                "编号", "时间", "类型", "结果", "概要", "详情");
        for (Log log : logs) {
            System.out.println(log);
        }
        System.out.println("------------------- 日志列表 -------------------");
        System.out.println("共计" + logs.size() + "条数据。");

        // 顺带统计成功与失败的条数
        int successCount = 0;
        for (Log log : logs) {
            if (log.isSuccess()) {
                successCount++;
            }
        }
        System.out.println("成功：" + successCount + " 条，失败：" + (logs.size() - successCount) + " 条。");
    }
}
