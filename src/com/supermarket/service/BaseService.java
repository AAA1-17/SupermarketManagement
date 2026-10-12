package com.supermarket.service;

import com.supermarket.store.DataStore;

/**
 * 业务服务基类：集中提供数据仓库与日志服务的引用，避免各服务重复声明。
 */
abstract class BaseService {

    protected final DataStore store = DataStore.getInstance();
    protected final LogService logService = LogService.getInstance();
}
