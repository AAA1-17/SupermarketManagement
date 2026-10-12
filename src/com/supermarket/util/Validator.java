package com.supermarket.util;

import java.util.regex.Pattern;

/**
 * 数据合法性校验工具（使用正则表达式）。
 */
public final class Validator {

    /** 手机号：11 位，1 开头，第二位 3-9 */
    private static final Pattern PHONE = Pattern.compile("^1[3-9]\\d{9}$");
    /** 年龄：1 ~ 150 */
    private static final Pattern AGE = Pattern.compile("^(?:[1-9]|[1-9]\\d|1[0-4]\\d|150)$");

    private Validator() {
    }

    /** 校验手机号 */
    public static boolean isPhone(String phone) {
        return phone != null && PHONE.matcher(phone).matches();
    }

    /** 校验年龄 */
    public static boolean isAge(String age) {
        return age != null && AGE.matcher(age).matches();
    }
}
