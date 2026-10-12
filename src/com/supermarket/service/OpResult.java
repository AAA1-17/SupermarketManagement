package com.supermarket.service;

/**
 * 业务操作结果：统一封装是否成功及提示信息。
 */
public record OpResult(boolean success, String message) {

    public static OpResult ok(String message) {
        return new OpResult(true, message);
    }

    public static OpResult fail(String message) {
        return new OpResult(false, message);
    }
}
