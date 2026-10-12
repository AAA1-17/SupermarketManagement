package com.supermarket.util;

import com.supermarket.enums.MenuItem;

/**
 * 菜单渲染与选择工具：基于枚举自动生成菜单并解析用户输入。
 */
public final class MenuUtil {

    private MenuUtil() {
    }

    /**
     * 渲染菜单并让用户选择，直到选择到合法的操作项为止。
     *
     * @param title 菜单标题
     * @param type  菜单枚举类型
     * @return 用户选择的操作枚举
     */
    public static <E extends Enum<E> & MenuItem> E choose(String title, Class<E> type) {
        E[] items = type.getEnumConstants();
        System.out.println();
        System.out.println("========== " + title + " ==========");
        for (E item : items) {
            System.out.printf("%d. %s%n", item.getCode(), item.getLabel());
        }
        while (true) {
            int code = InputUtil.nextInt("请选择操作：");
            for (E item : items) {
                if (item.getCode() == code) {
                    return item;
                }
            }
            System.out.println("输入的操作编号不存在，请重新选择！");
        }
    }
}
