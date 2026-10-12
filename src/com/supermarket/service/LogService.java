package com.supermarket.service;

import com.supermarket.entity.Log;
import com.supermarket.enums.LogType;
import com.supermarket.store.DataStore;
import com.supermarket.util.IdGenerator;

import java.util.Date;
import java.util.List;

/**
 * 日志管理：记录系统操作日志。
 */
public class LogService {

    private static final LogService INSTANCE = new LogService();

    private final DataStore store = DataStore.getInstance();

    private LogService() {
    }

    public static LogService getInstance() {
        return INSTANCE;
    }

    /** 记录一条日志 */
    public void log(LogType type, String title, String description, boolean success) {
        String id = IdGenerator.nextId("L", 4, store.getLogs(), Log::getId);
        store.getLogs().add(new Log(id, title, description, new Date(), success, type));
        store.saveLogs();
    }

    /** 查询全部日志 */
    public List<Log> listAll() {
        return store.getLogs();
    }
}
