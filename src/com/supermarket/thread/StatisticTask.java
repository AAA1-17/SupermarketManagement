package com.supermarket.thread;

import com.supermarket.service.OrderService;
import com.supermarket.service.ProductService;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 统计报表线程：使用额外的线程，每分钟统计一次库存与销售情况并打印。
 */
public final class StatisticTask {

    /** 统计周期：每分钟一次 */
    private static final long PERIOD_MILLIS = 60_000L;

    private StatisticTask() {
    }

    /** 启动库存统计线程与销售统计线程（均为守护线程，随主程序退出而结束） */
    public static void start(ProductService productService, OrderService orderService) {
        Thread inventoryThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                printInventory(productService);
                if (!sleep()) {
                    return;
                }
            }
        }, "库存统计线程");
        inventoryThread.setDaemon(true);
        inventoryThread.start();

        Thread salesThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                printSales(orderService);
                if (!sleep()) {
                    return;
                }
            }
        }, "销售统计线程");
        salesThread.setDaemon(true);
        salesThread.start();
    }

    private static boolean sleep() {
        try {
            Thread.sleep(PERIOD_MILLIS);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /** 统计当前库存产品 */
    private static void printInventory(ProductService productService) {
        int count = productService.totalStockCount();
        BigDecimal amount = productService.totalStockAmount();
        BigDecimal avg = average(amount, count);
        System.out.println();
        System.out.println("当前物品总数：" + count + "，物品总金额" + amount + "，物品平均价格" + avg);
    }

    /** 统计当前销售产品 */
    private static void printSales(OrderService orderService) {
        int count = orderService.totalSalesQuantity();
        BigDecimal amount = orderService.totalSalesAmount();
        BigDecimal avg = average(amount, count);
        System.out.println();
        System.out.println("当前销售物品总数：" + count + "，销售物品总金额" + amount + "，物品平均价格" + avg);
    }

    private static BigDecimal average(BigDecimal total, int count) {
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }
}
