package com.supermarket.entity;

import java.util.ArrayList;
import java.util.List;

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

    // ==================== 测试用例 ====================

    /** 直接运行本方法可单独测试商品类别实体 */
    public static void main(String[] args) {
        List<ProductType> types = new ArrayList<>();
        types.add(new ProductType("T0001", "饮料"));
        types.add(new ProductType("T0002", "零食"));
        types.add(new ProductType("T0003", "生鲜"));
        types.add(new ProductType("T0004", "日用品"));
        types.add(new ProductType("T0005", "粮油"));

        System.out.println("----------------- 商品类别列表 -----------------");
        System.out.printf("%-6s %s%n", "编号", "类别名称");
        for (ProductType type : types) {
            System.out.println(type);
        }
        System.out.println("----------------- 商品类别列表 -----------------");
        System.out.println("共计" + types.size() + "条数据。");
    }
}
