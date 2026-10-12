package com.supermarket.service;

import com.supermarket.entity.Order;
import com.supermarket.entity.Product;
import com.supermarket.enums.LogType;
import com.supermarket.util.Finder;
import com.supermarket.util.IdGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单管理：新增订单、订单列表、移除订单、查找订单，以及销售统计。
 */
public class OrderService extends BaseService {

    private final ProductService productService = new ProductService();

    /** 新增订单结果 */
    public record CreateOrderResult(boolean success, String message, Order order) {
    }

    /**
     * 新增订单：根据商品编号与数量扣减库存、计算总价，并记录收银员与收银时间。
     */
    public CreateOrderResult createOrder(String cashierId, Map<String, Integer> items) {
        if (items == null || items.isEmpty()) {
            return new CreateOrderResult(false, "订单商品不能为空", null);
        }
        // 先校验所有商品是否存在、库存是否充足
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = productService.findProductById(entry.getKey());
            if (product == null) {
                logService.log(LogType.ORDER, "新增订单失败", "商品不存在：" + entry.getKey(), false);
                return new CreateOrderResult(false, "商品不存在：" + entry.getKey(), null);
            }
            if (entry.getValue() == null || entry.getValue() <= 0) {
                return new CreateOrderResult(false, "商品数量必须大于 0：" + entry.getKey(), null);
            }
            if (product.getCount() < entry.getValue()) {
                logService.log(LogType.ORDER, "新增订单失败",
                        "库存不足：" + product.getName() + "，现有 " + product.getCount(), false);
                return new CreateOrderResult(false,
                        "库存不足，商品【" + product.getName() + "】现有数量：" + product.getCount(), null);
            }
        }
        // 扣减库存并计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = productService.findProductById(entry.getKey());
            product.setCount(product.getCount() - entry.getValue());
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        store.saveProducts();

        String id = IdGenerator.nextId("O", 4, store.getOrders(), Order::getId);
        Order order = new Order(id, cashierId, new LinkedHashMap<>(items), totalPrice, new Date());
        store.getOrders().add(order);
        store.saveOrders();
        logService.log(LogType.ORDER, "新增订单",
                "订单号：" + id + "，收银员：" + cashierId + "，总价：" + totalPrice, true);
        return new CreateOrderResult(true,
                "下单成功，订单号：" + id + "，总价：" + totalPrice, order);
    }

    public List<Order> listAll() {
        return store.getOrders();
    }

    public Order findById(String id) {
        return Finder.find(store.getOrders(), id, Order::getId);
    }

    public OpResult removeById(String id) {
        Order target = findById(id);
        if (target == null) {
            logService.log(LogType.ORDER, "移除订单失败", "订单不存在：" + id, false);
            return OpResult.fail("订单不存在：" + id);
        }
        store.getOrders().remove(target);
        store.saveOrders();
        logService.log(LogType.ORDER, "移除订单", "移除订单：" + id, true);
        return OpResult.ok("移除订单成功：" + id);
    }

    /** 销售商品总数量 */
    public int totalSalesQuantity() {
        int sum = 0;
        for (Order order : store.getOrders()) {
            sum += order.getTotalQuantity();
        }
        return sum;
    }

    /** 销售商品总金额 */
    public BigDecimal totalSalesAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Order order : store.getOrders()) {
            sum = sum.add(order.getTotalPrice());
        }
        return sum;
    }
}
