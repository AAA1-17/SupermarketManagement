package com.supermarket;

import com.supermarket.store.DataStore;
import com.supermarket.ui.ConsoleApp;

/**
 * 超市管理系统入口。
 */
public class Main {

    public static void main(String[] args) {
        // 启动时加载已持久化的数据，保证关闭程序后下次打开数据依然存在
        DataStore.getInstance().load();
        new ConsoleApp().start();
    }
}
