package com.supermarket.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    /** 该商品的库存金额（单价 × 数量） */
    public BigDecimal getStockAmount() {
        return price.multiply(BigDecimal.valueOf(count));
    }

    @Override
    public String toString() {
        return String.format("%-6s %-12s %-10s %-8d %s",
                id, name, price, count, typeId);
    }

    // ==================== 测试用例 ====================

    /** 直接运行本方法可单独测试商品实体 */
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        products.add(new Product("P0001", "可乐", new BigDecimal("3.5"), 100, "T0001"));
        products.add(new Product("P0002", "雪碧", new BigDecimal("3.0"), 80, "T0001"));
        products.add(new Product("P0003", "矿泉水", new BigDecimal("1.5"), 200, "T0001"));
        products.add(new Product("P0004", "薯片", new BigDecimal("6.5"), 60, "T0002"));
        products.add(new Product("P0005", "巧克力", new BigDecimal("12.5"), 40, "T0002"));
        products.add(new Product("P0006", "苹果", new BigDecimal("5.8"), 120, "T0003"));

        System.out.println("------------------- 商品列表 -------------------");
        System.out.printf("%-6s %-12s %-10s %-8s %s%n", "编号", "名称", "价格", "数量", "类型编号");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("------------------- 商品列表 -------------------");
        System.out.println("共计" + products.size() + "条数据。");

        // 顺带统计库存，验证单价与数量的计算
        int totalCount = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Product product : products) {
            totalCount += product.getCount();
            totalAmount = totalAmount.add(product.getStockAmount());
        }
        BigDecimal average = totalCount == 0 ? BigDecimal.ZERO
                : totalAmount.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
        System.out.println("库存总数量：" + totalCount + "，库存总金额：" + totalAmount
                + "，商品平均价格：" + average);

        // 演示 setter：给可乐降价
        products.get(0).setPrice(new BigDecimal("3.2"));
        System.out.println("调价后：" + products.get(0));
    }
}
