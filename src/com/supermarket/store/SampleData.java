package com.supermarket.store;

import com.supermarket.entity.Cashier;
import com.supermarket.entity.Log;
import com.supermarket.entity.Order;
import com.supermarket.entity.Product;
import com.supermarket.entity.ProductType;
import com.supermarket.enums.LogType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 示例数据：首次启动（没有任何数据文件）时自动导入，
 * 保证收银员列表、商品列表等菜单一打开就能看到数据。
 */
public final class SampleData {

    private SampleData() {
    }

    /** 把示例数据填充到数据仓库（不负责写盘） */
    public static void seed(DataStore store) {
        seedTypes(store);
        seedCashiers(store);
        seedProducts(store);
        seedOrders(store);
        seedLogs(store);
    }

    private static void seedTypes(DataStore store) {
        store.getTypes().add(new ProductType("T0001", "饮料"));
        store.getTypes().add(new ProductType("T0002", "零食"));
        store.getTypes().add(new ProductType("T0003", "生鲜"));
        store.getTypes().add(new ProductType("T0004", "日用品"));
        store.getTypes().add(new ProductType("T0005", "粮油"));
    }

    private static void seedCashiers(DataStore store) {
        store.getCashiers().add(new Cashier("C0001", "92930120027", "p4i5DR86", "端木玉", '女', 32,
                "河南省平顶山市鲁山县原平乡田土村民长组50号", "13290023466"));
        store.getCashiers().add(new Cashier("C0002", "46249851193", "35wL59Cs", "水永景", '女', 34,
                "黑龙江省大庆市龙凤区星采中路34号塞纳新天地1栋1单元1704房", "16215216549"));
        store.getCashiers().add(new Cashier("C0003", "83829839551", "7218szUB", "路影", '女', 64,
                "山东省威海市环翠区胜丰里西路438号田园汇8栋1单元0801房", "16902239467"));
        store.getCashiers().add(new Cashier("C0004", "15366753375", "920Rt0w8", "司空波修", '女', 15,
                "青海省果洛藏族自治州甘德县兰天乡八班村泵湘组83号", "18639911577"));
        store.getCashiers().add(new Cashier("C0005", "65923562653", "bE5qK922", "桑葳", '男', 37,
                "陕西省汉中市勉县乐林镇汉村永九组26号", "15784143653"));
    }

    private static void seedProducts(DataStore store) {
        store.getProducts().add(new Product("P0001", "可乐", new BigDecimal("3.5"), 100, "T0001"));
        store.getProducts().add(new Product("P0002", "雪碧", new BigDecimal("3.0"), 80, "T0001"));
        store.getProducts().add(new Product("P0003", "矿泉水", new BigDecimal("1.5"), 200, "T0001"));
        store.getProducts().add(new Product("P0004", "薯片", new BigDecimal("6.5"), 60, "T0002"));
        store.getProducts().add(new Product("P0005", "巧克力", new BigDecimal("12.5"), 40, "T0002"));
        store.getProducts().add(new Product("P0006", "苹果", new BigDecimal("5.8"), 120, "T0003"));
    }

    private static void seedOrders(DataStore store) {
        Map<String, Integer> items1 = new LinkedHashMap<>();
        items1.put("P0001", 2);
        items1.put("P0004", 1);
        store.getOrders().add(new Order("O0001", "C0001", items1, new BigDecimal("13.5"), new Date()));

        Map<String, Integer> items2 = new LinkedHashMap<>();
        items2.put("P0003", 5);
        items2.put("P0006", 3);
        store.getOrders().add(new Order("O0002", "C0002", items2, new BigDecimal("24.9"), new Date()));
    }

    private static void seedLogs(DataStore store) {
        store.getLogs().add(new Log("L0001", "系统初始化", "首次启动，已自动导入示例数据",
                new Date(), true, LogType.SYSTEM));
        store.getLogs().add(new Log("L0002", "新增收银员", "导入示例收银员 5 名",
                new Date(), true, LogType.CASHIER));
        store.getLogs().add(new Log("L0003", "新增商品", "导入示例商品 6 件",
                new Date(), true, LogType.PRODUCT));
    }
}
