package com.supermarket.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 简易 CSV 工具：负责把一行数据拆成字段、把字段拼成一行。
 * 支持逗号分隔与双引号转义，保证名称、地址等含有逗号时也能正确存取。
 */
public final class CsvUtil {

    private static final char SEPARATOR = ',';
    private static final char QUOTE = '"';

    private CsvUtil() {
    }

    /** 把多个字段拼接成一行 CSV 文本 */
    public static String toLine(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.append(SEPARATOR);
            }
            sb.append(escape(fields[i]));
        }
        return sb.toString();
    }

    /** 解析一行 CSV 文本为字段列表 */
    public static List<String> parse(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == QUOTE) {
                    // 连续两个引号表示一个转义引号
                    if (i + 1 < line.length() && line.charAt(i + 1) == QUOTE) {
                        current.append(QUOTE);
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else if (c == QUOTE) {
                inQuotes = true;
            } else if (c == SEPARATOR) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.indexOf(SEPARATOR) >= 0 || value.indexOf(QUOTE) >= 0
                || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
            return QUOTE + value.replace(String.valueOf(QUOTE), String.valueOf(QUOTE) + QUOTE) + QUOTE;
        }
        return value;
    }
}
