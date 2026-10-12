package com.supermarket.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 订单实体
 */
public class Order {

    /** 订单号（系统自动生成） */
    private final String id;
    /** 收银员ID */
    private final String cashierId;
    /** 产品列表：key 为产品ID，value 为数量 */
    private final Map<String, Integer> product;
    /** 总价 */
    private final BigDecimal totalPrice;
    /** 收银日期 */
    private final Date date;

    public Order(String id, String cashierId, Map<String, Integer> product,
                 BigDecimal totalPrice, Date date) {
        this.id = id;
        this.cashierId = cashierId;
        this.product = product == null ? new LinkedHashMap<>() : product;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getCashierId() {
        return cashierId;
    }

    public Map<String, Integer> getProduct() {
        return product;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Date getDate() {
        return date;
    }

    /** 订单明细中商品总数量 */
    public int getTotalQuantity() {
        int sum = 0;
        for (Integer quantity : product.values()) {
            sum += (quantity == null ? 0 : quantity);
        }
        return sum;
    }

    @Override
    public String toString() {
        String time = date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return String.format("订单号:%-8s 收银员:%-6s 总价:%-8s 时间:%s 商品:%s",
                id, cashierId, totalPrice, time, product);
    }

    // ==================== 测试用例 ====================

    /** 直接运行本方法可单独测试订单实体 */
    public static void main(String[] args) {
        Order order1 = new Order("O0001", "C0001", items("P0001", 2, "P0004", 1),
                new BigDecimal("13.5"), new Date());
        Order order2 = new Order("O0002", "C0002", items("P0003", 5, "P0006", 3),
                new BigDecimal("24.9"), new Date());

        System.out.println("------------------- 订单列表 -------------------");
        System.out.printf("%-8s %-8s %-10s %-20s %s%n", "订单号", "收银员", "总价", "收银时间", "商品明细");
        for (Order order : new Order[]{order1, order2}) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getDate());
            System.out.printf("%-8s %-8s %-10s %-20s %s%n",
                    order.getId(), order.getCashierId(), order.getTotalPrice(), time, order.getProduct());
        }
        System.out.println("------------------- 订单列表 -------------------");
        System.out.println("共计2条数据。");

        // 顺带统计销售情况
        int totalQuantity = order1.getTotalQuantity() + order2.getTotalQuantity();
        BigDecimal totalAmount = order1.getTotalPrice().add(order2.getTotalPrice());
        System.out.println("销售商品总数：" + totalQuantity + "，销售总金额：" + totalAmount);
    }

    /** 快速构造商品明细的辅助方法（仅供测试用例使用） */
    private static Map<String, Integer> items(String id1, int count1, String id2, int count2) {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put(id1, count1);
        map.put(id2, count2);
        return map;
    }
}
