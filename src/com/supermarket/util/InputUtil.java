package com.supermarket.util;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * 控制台输入工具：统一使用 Scanner 读取用户输入，并对非法输入做循环校验。
 */
public final class InputUtil {

    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtil() {
    }

    /** 读取一行文本（可为空） */
    public static String nextLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    /** 读取非空文本 */
    public static String nextNonEmpty(String prompt) {
        while (true) {
            String value = nextLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("输入不能为空，请重新输入！");
        }
    }

    /** 读取整数 */
    public static int nextInt(String prompt) {
        while (true) {
            String value = nextLine(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入整数！");
            }
        }
    }

    /** 读取正整数（大于 0） */
    public static int nextPositiveInt(String prompt) {
        while (true) {
            int value = nextInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("请输入大于 0 的正整数！");
        }
    }

    /** 读取金额（大于等于 0 的小数） */
    public static BigDecimal nextBigDecimal(String prompt) {
        while (true) {
            String value = nextLine(prompt);
            try {
                BigDecimal decimal = new BigDecimal(value);
                if (decimal.signum() < 0) {
                    System.out.println("金额不能为负数，请重新输入！");
                    continue;
                }
                return decimal;
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入合法的金额！");
            }
        }
    }
}
