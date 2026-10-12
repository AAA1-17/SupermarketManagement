package com.supermarket.enums;

/**
 * 日志类型枚举：对应各种类型的日志，方便管理
 */
public enum LogType {

    LOGIN(1, "登录"),
    CASHIER(2, "收银员管理"),
    PRODUCT(3, "商品管理"),
    PRODUCT_TYPE(4, "商品类别管理"),
    ORDER(5, "订单管理"),
    SYSTEM(6, "系统");

    private final int code;
    private final String label;

    LogType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
