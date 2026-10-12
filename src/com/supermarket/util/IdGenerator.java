package com.supermarket.util;

import java.util.Collection;
import java.util.function.Function;

/**
 * ID 自动生成工具：根据已有对象的 ID 集合，计算下一个可用编号。
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    /**
     * 生成下一个 ID，格式为 前缀 + 补零数字。
     *
     * @param prefix   前缀，如 C、P、T、O、L
     * @param width    数字位数，不足补零
     * @param items    已有对象集合
     * @param idGetter 从对象中取出 ID 的方法，如 {@code Cashier::getId}
     */
    public static <T> String nextId(String prefix, int width, Collection<T> items,
                                    Function<T, String> idGetter) {
        int max = 0;
        if (items != null) {
            for (T item : items) {
                String id = idGetter.apply(item);
                if (id != null && id.startsWith(prefix)) {
                    try {
                        max = Math.max(max, Integer.parseInt(id.substring(prefix.length())));
                    } catch (NumberFormatException ignored) {
                        // 忽略非标准格式的历史数据
                    }
                }
            }
        }
        return prefix + String.format("%0" + width + "d", max + 1);
    }
}
