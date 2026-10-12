package com.supermarket.entity;

import java.math.BigDecimal;

/**
 * 商品实体
 */
public class Product {

    /** 产品编号（系统自动生成） */
    private final String id;
    /** 产品名称 */
    private final String name;
    /** 价格 */
    private BigDecimal price;
    /** 商品数量 */
    private int count;
    /** 所属类别ID */
    private final String typeId;

    public Product(String id, String name, BigDecimal price, int count, String typeId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.typeId = typeId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTypeId() {
        return typeId;
    }

    @Override
    public String toString() {
        return String.format("%-6s %-12s %-10s %-8d %s",
                id, name, price, count, typeId);
    }
}
