package com.supermarket;

import com.supermarket.store.DataStore;
import com.supermarket.store.SampleData;
import com.supermarket.ui.ConsoleApp;

/**
 * 超市管理系统入口。
 */
public class Main {

    public static void main(String[] args) {
        // 启动时加载已持久化的数据，保证关闭程序后下次打开数据依然存在
        DataStore store = DataStore.getInstance();
        store.load();
        // 首次启动（没有任何数据）时自动导入示例数据，避免各列表空空如也
        if (store.isEmpty()) {
            SampleData.seed(store);
            store.saveAll();
            System.out.println("首次启动，已自动导入示例数据。");
        }
        new ConsoleApp().start();
    }
}
