package com.supermarket.entity;

/**
 * 商品类别实体
 */
public class ProductType {

    /** 产品类别编号（系统自动生成） */
    private final String id;
    /** 产品类别名称 */
    private final String name;

    public ProductType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%-6s %s", id, name);
    }
}
