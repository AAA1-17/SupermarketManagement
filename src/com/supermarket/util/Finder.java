package com.supermarket.util;

import java.util.Collection;
import java.util.function.Function;

/**
 * 通用查找工具：按某个字符串字段（如 ID、名称）在集合中查找对象。
 */
public final class Finder {

    private Finder() {
    }

    /** 返回集合中字段值等于 value 的第一个对象，找不到返回 null */
    public static <T> T find(Collection<T> items, String value, Function<T, String> fieldGetter) {
        for (T item : items) {
            if (fieldGetter.apply(item).equals(value)) {
                return item;
            }
        }
        return null;
    }
}
