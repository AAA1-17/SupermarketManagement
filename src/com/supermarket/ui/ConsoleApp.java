package com.supermarket.ui;

import com.supermarket.entity.Cashier;
import com.supermarket.entity.Log;
import com.supermarket.entity.Order;
import com.supermarket.entity.Product;
import com.supermarket.entity.ProductType;
import com.supermarket.enums.Menus;
import com.supermarket.service.AuthService;
import com.supermarket.service.CashierService;
import com.supermarket.service.LogService;
import com.supermarket.service.OpResult;
import com.supermarket.service.OrderService;
import com.supermarket.service.ProductService;
import com.supermarket.thread.StatisticTask;
import com.supermarket.util.InputUtil;
import com.supermarket.util.MenuUtil;
import com.supermarket.util.Validator;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制台界面：负责菜单渲染与用户交互，具体业务交由各 Service 处理。
 */
public class ConsoleApp {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final AuthService authService = new AuthService();
    private final CashierService cashierService = new CashierService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final LogService logService = LogService.getInstance();

    /** 程序入口流程 */
    public void start() {
        printBanner();
        if (!systemLogin()) {
            System.out.println("登录失败次数过多，程序退出！");
            return;
        }
        System.out.println("\n登录成功，欢迎使用超市管理系统！");
        StatisticTask.start(productService, orderService);
        mainLoop();
    }

    private void printBanner() {
        System.out.println("******************************************");
        System.out.println("*           超 市 管 理 系 统            *");
        System.out.println("******************************************");
    }

    // ==================== 登录 ====================

    private boolean systemLogin() {
        for (int i = 1; i <= MAX_LOGIN_ATTEMPTS; i++) {
            System.out.println("\n---------- 系统登录（第 " + i + " / " + MAX_LOGIN_ATTEMPTS + " 次）----------");
            String username = InputUtil.nextNonEmpty("请输入用户名：");
            String password = InputUtil.nextNonEmpty("请输入密码：");
            if (authService.login(username, password)) {
                return true;
            }
            System.out.println("用户名或密码错误！");
        }
        return false;
    }

    // ==================== 主菜单 ====================

    private void mainLoop() {
        while (true) {
            Menus.Main choice = MenuUtil.choose("主菜单", Menus.Main.class);
            switch (choice) {
                case SYSTEM -> systemModule();
                case PRODUCT -> productModule();
                case CASHIER -> cashierModule();
                case EXIT -> {
                    System.out.println("感谢使用，再见！");
                    return;
                }
            }
        }
    }

    // ==================== 系统管理模块 ====================

    private void systemModule() {
        while (true) {
            Menus.System choice = MenuUtil.choose("系统管理模块", Menus.System.class);
            switch (choice) {
                case CASHIER_MANAGE -> cashierManage();
                case LOG_MANAGE -> logManage();
                case BACK -> {
                    return;
                }
            }
        }
    }

    private void cashierManage() {
        while (true) {
            Menus.CashierManage choice = MenuUtil.choose("收银员管理", Menus.CashierManage.class);
            switch (choice) {
                case LIST -> printCashiers();
                case ADD -> addCashier();
                case REMOVE -> removeCashier();
                case BACK -> {
                    return;
                }
            }
        }
    }

    private void logManage() {
        while (true) {
            Menus.LogManage choice = MenuUtil.choose("日志管理", Menus.LogManage.class);
            switch (choice) {
                case LIST -> printLogs();
                case BACK -> {
                    return;
                }
            }
        }
    }

    // ==================== 商品管理模块 ====================

    private void productModule() {
        while (true) {
            Menus.Product choice = MenuUtil.choose("商品管理模块", Menus.Product.class);
            switch (choice) {
                case PRODUCT_MANAGE -> productManage();
                case TYPE_MANAGE -> typeManage();
                case BACK -> {
                    return;
                }
            }
        }
    }

    private void productManage() {
        while (true) {
            Menus.ProductManage choice = MenuUtil.choose("商品管理", Menus.ProductManage.class);
            switch (choice) {
                case LIST -> printProducts();
                case ADD -> addProduct();
                case REMOVE -> removeProduct();
                case UPDATE_PRICE -> updatePrice();
                case STOCK_IN -> stockIn();
                case STOCK_OUT -> stockOut();
                case BACK -> {
                    return;
                }
            }
        }
    }

    private void typeManage() {
        while (true) {
            Menus.TypeManage choice = MenuUtil.choose("商品类别管理", Menus.TypeManage.class);
            switch (choice) {
                case LIST -> printTypes();
                case ADD -> addType();
                case REMOVE -> removeType();
                case BACK -> {
                    return;
                }
            }
        }
    }

    // ==================== 收银模块 ====================

    private void cashierModule() {
        while (true) {
            Menus.CashierModule choice = MenuUtil.choose("收银模块", Menus.CashierModule.class);
            switch (choice) {
                case LOGIN -> {
                    Cashier cashier = cashierLogin();
                    if (cashier != null) {
                        System.out.println("登录成功，欢迎收银员：" + cashier.getName());
                        orderManage(cashier);
                    }
                }
                case BACK -> {
                    return;
                }
            }
        }
    }

    private Cashier cashierLogin() {
        System.out.println("\n---------- 收银员登录 ----------");
        String account = InputUtil.nextNonEmpty("请输入收银员账号：");
        String password = InputUtil.nextNonEmpty("请输入收银员密码：");
        Cashier cashier = authService.cashierLogin(account, password);
        if (cashier == null) {
            System.out.println("收银员账号或密码错误！");
        }
        return cashier;
    }

    private void orderManage(Cashier cashier) {
        while (true) {
            Menus.OrderManage choice = MenuUtil.choose("订单管理", Menus.OrderManage.class);
            switch (choice) {
                case ADD -> addOrder(cashier);
                case LIST -> printOrders();
                case REMOVE -> removeOrder();
                case FIND -> findOrder();
                case BACK -> {
                    return;
                }
            }
        }
    }

    // ==================== 收银员管理操作 ====================

    private void addCashier() {
        System.out.println("\n---------- 新增收银员 ----------");
        String account = InputUtil.nextNonEmpty("请输入账号：");
        String password = InputUtil.nextNonEmpty("请输入密码：");
        String sexStr = InputUtil.nextNonEmpty("请输入性别（男/女）：");
        char sex = sexStr.charAt(0);
        int age = readAge();
        String name = InputUtil.nextNonEmpty("请输入姓名：");
        String phone = readPhone();
        String address = InputUtil.nextNonEmpty("请输入地址：");
        OpResult result = cashierService.add(account, password, name, sex, age, address, phone);
        System.out.println(result.message());
    }

    private void removeCashier() {
        System.out.println("\n---------- 移除收银员 ----------");
        if (cashierService.listAll().isEmpty()) {
            System.out.println("暂无收银员数据！");
            return;
        }
        String id = InputUtil.nextNonEmpty("请输入要移除的收银员ID：");
        System.out.println(cashierService.removeById(id).message());
    }

    private int readAge() {
        while (true) {
            String age = InputUtil.nextLine("请输入年龄：");
            if (Validator.isAge(age)) {
                return Integer.parseInt(age);
            }
            System.out.println("年龄不合法（应为 1-150 的整数），请重新输入！");
        }
    }

    private String readPhone() {
        while (true) {
            String phone = InputUtil.nextLine("请输入联系方式：");
            if (Validator.isPhone(phone)) {
                return phone;
            }
            System.out.println("手机号不合法（应为 1 开头的 11 位数字），请重新输入！");
        }
    }

    // ==================== 商品操作 ====================

    private void addProduct() {
        System.out.println("\n---------- 新增商品 ----------");
        printTypes();
        String name = InputUtil.nextNonEmpty("请输入商品名称：");
        BigDecimal price = InputUtil.nextBigDecimal("请输入商品价格：");
        int count = InputUtil.nextPositiveInt("请输入商品数量：");
        String typeId = InputUtil.nextNonEmpty("请输入商品类型编号：");
        System.out.println(productService.addProduct(name, price, count, typeId).message());
    }

    private void removeProduct() {
        System.out.println("\n---------- 移除商品 ----------");
        String id = InputUtil.nextNonEmpty("请输入商品ID：");
        System.out.println(productService.removeProduct(id).message());
    }

    private void updatePrice() {
        System.out.println("\n---------- 修改商品价格 ----------");
        String id = InputUtil.nextNonEmpty("请输入商品编号：");
        BigDecimal price = InputUtil.nextBigDecimal("请输入新的价格：");
        System.out.println(productService.updatePrice(id, price).message());
    }

    private void stockIn() {
        System.out.println("\n---------- 商品入库 ----------");
        String id = InputUtil.nextNonEmpty("请输入商品编号：");
        int quantity = InputUtil.nextPositiveInt("请输入入库数量：");
        System.out.println(productService.stockIn(id, quantity).message());
    }

    private void stockOut() {
        System.out.println("\n---------- 商品出库 ----------");
        String id = InputUtil.nextNonEmpty("请输入商品编号：");
        int quantity = InputUtil.nextPositiveInt("请输入出库数量：");
        System.out.println(productService.stockOut(id, quantity).message());
    }

    private void addType() {
        System.out.println("\n---------- 新增商品类别 ----------");
        String name = InputUtil.nextNonEmpty("请输入商品类别名称：");
        System.out.println(productService.addType(name).message());
    }

    private void removeType() {
        System.out.println("\n---------- 移除商品类别 ----------");
        String id = InputUtil.nextNonEmpty("请输入商品类别编号：");
        System.out.println(productService.removeType(id).message());
    }

    // ==================== 订单操作 ====================

    private void addOrder(Cashier cashier) {
        System.out.println("\n---------- 新增订单 ----------");
        printProducts();
        Map<String, Integer> items = new LinkedHashMap<>();
        while (true) {
            String productId = InputUtil.nextNonEmpty("请输入商品编号（输入 0 结束下单）：");
            if (productId.equals("0")) {
                break;
            }
            Product product = productService.findProductById(productId);
            if (product == null) {
                System.out.println("商品不存在，请重新输入！");
                continue;
            }
            int quantity = InputUtil.nextPositiveInt("请输入商品数量：");
            items.merge(productId, quantity, Integer::sum);
            System.out.println("已添加：" + product.getName() + " x " + quantity);
        }
        OrderService.CreateOrderResult result = orderService.createOrder(cashier.getId(), items);
        System.out.println(result.message());
    }

    private void removeOrder() {
        System.out.println("\n---------- 移除订单 ----------");
        String id = InputUtil.nextNonEmpty("请输入订单ID：");
        System.out.println(orderService.removeById(id).message());
    }

    private void findOrder() {
        System.out.println("\n---------- 查找订单 ----------");
        String id = InputUtil.nextNonEmpty("请输入订单ID：");
        Order order = orderService.findById(id);
        if (order == null) {
            System.out.println("订单不存在：" + id);
            return;
        }
        System.out.println("订单号：" + order.getId());
        System.out.println("收银员：" + order.getCashierId());
        System.out.println("总价：" + order.getTotalPrice());
        System.out.println("收银日期：" + order.getDate());
        System.out.println("商品明细：");
        for (Map.Entry<String, Integer> entry : order.getProduct().entrySet()) {
            Product product = productService.findProductById(entry.getKey());
            String name = product == null ? "已删除商品" : product.getName();
            System.out.printf("  %s %s x %d%n", entry.getKey(), name, entry.getValue());
        }
    }

    // ==================== 列表打印 ====================

    private void printCashiers() {
        List<Cashier> cashiers = cashierService.listAll();
        System.out.println("\n---------- 收银员列表 ----------");
        if (cashiers.isEmpty()) {
            System.out.println("暂无收银员数据！");
            return;
        }
        System.out.printf("%-6s %-10s %-8s %-3s %-4s %-13s %-10s %s%n",
                "ID", "账号", "姓名", "性别", "年龄", "联系方式", "地址", "密码");
        for (Cashier cashier : cashiers) {
            System.out.println(cashier);
        }
    }

    private void printProducts() {
        List<Product> products = productService.listProducts();
        System.out.println("\n---------- 商品列表 ----------");
        if (products.isEmpty()) {
            System.out.println("暂无商品数据！");
            return;
        }
        System.out.printf("%-6s %-12s %-10s %-8s %s%n",
                "编号", "名称", "价格", "数量", "类型编号");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void printTypes() {
        List<ProductType> types = productService.listTypes();
        System.out.println("\n---------- 商品类别列表 ----------");
        if (types.isEmpty()) {
            System.out.println("暂无商品类别数据！");
            return;
        }
        System.out.printf("%-6s %s%n", "编号", "类别名称");
        for (ProductType type : types) {
            System.out.println(type);
        }
    }

    private void printOrders() {
        List<Order> orders = orderService.listAll();
        System.out.println("\n---------- 订单列表 ----------");
        if (orders.isEmpty()) {
            System.out.println("暂无订单数据！");
            return;
        }
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    private void printLogs() {
        List<Log> logs = logService.listAll();
        System.out.println("\n---------- 日志列表 ----------");
        if (logs.isEmpty()) {
            System.out.println("暂无日志数据！");
            return;
        }
        System.out.printf("%-8s %-19s %-10s %-4s %-18s %s%n",
                "编号", "时间", "类型", "结果", "概要", "详情");
        for (Log log : logs) {
            System.out.println(log);
        }
    }
}
