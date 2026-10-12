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
}
