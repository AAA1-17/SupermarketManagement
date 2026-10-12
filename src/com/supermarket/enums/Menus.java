package com.supermarket.enums;

/**
 * 系统所有操作菜单枚举定义。
 * 使用枚举实现不同的操作，配合 MenuItem 接口由 MenuUtil 统一渲染。
 */
public final class Menus {

    private Menus() {
    }

    /** 主菜单 */
    public enum Main implements MenuItem {
        SYSTEM(1, "系统管理模块"),
        PRODUCT(2, "商品管理模块"),
        CASHIER(3, "收银模块"),
        EXIT(0, "退出系统");

        private final int code;
        private final String label;

        Main(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 系统管理模块菜单 */
    public enum System implements MenuItem {
        CASHIER_MANAGE(1, "收银员管理"),
        LOG_MANAGE(2, "日志管理"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        System(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 收银员管理菜单 */
    public enum CashierManage implements MenuItem {
        LIST(1, "收银员列表"),
        ADD(2, "新增收银员"),
        REMOVE(3, "移除收银员"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        CashierManage(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 日志管理菜单 */
    public enum LogManage implements MenuItem {
        LIST(1, "日志列表"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        LogManage(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 商品管理模块菜单 */
    public enum Product implements MenuItem {
        PRODUCT_MANAGE(1, "商品管理"),
        TYPE_MANAGE(2, "商品类别管理"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        Product(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 商品管理菜单 */
    public enum ProductManage implements MenuItem {
        LIST(1, "商品列表"),
        ADD(2, "新增商品"),
        REMOVE(3, "移除商品"),
        UPDATE_PRICE(4, "修改商品价格"),
        STOCK_IN(5, "商品入库"),
        STOCK_OUT(6, "商品出库"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        ProductManage(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 商品类别管理菜单 */
    public enum TypeManage implements MenuItem {
        LIST(1, "商品类别列表"),
        ADD(2, "新增商品类别"),
        REMOVE(3, "移除商品类别"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        TypeManage(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 收银模块入口菜单 */
    public enum CashierModule implements MenuItem {
        LOGIN(1, "收银员登录"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        CashierModule(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    /** 收银员登录后的订单操作菜单 */
    public enum OrderManage implements MenuItem {
        ADD(1, "新增订单"),
        LIST(2, "订单列表"),
        REMOVE(3, "移除订单"),
        FIND(4, "查找订单"),
        BACK(0, "返回上一级");

        private final int code;
        private final String label;

        OrderManage(int code, String label) {
            this.code = code;
            this.label = label;
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }
}
