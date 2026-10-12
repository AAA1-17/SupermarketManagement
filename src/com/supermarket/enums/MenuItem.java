package com.supermarket.enums;

/**
 * 菜单项统一接口，所有操作菜单枚举都实现该接口，
 * 便于用统一的方式渲染菜单与解析用户输入。
 */
public interface MenuItem {

    /** 操作编号 */
    int getCode();

    /** 操作描述 */
    String getLabel();
}
