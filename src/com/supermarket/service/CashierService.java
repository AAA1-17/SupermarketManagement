package com.supermarket.service;

import com.supermarket.entity.Cashier;
import com.supermarket.enums.LogType;
import com.supermarket.util.Finder;
import com.supermarket.util.IdGenerator;

import java.util.List;

/**
 * 收银员管理：列表、新增、移除。
 */
public class CashierService extends BaseService {

    public List<Cashier> listAll() {
        return store.getCashiers();
    }

    /** 账号是否已存在 */
    public boolean existsAccount(String account) {
        return Finder.find(store.getCashiers(), account, Cashier::getAccount) != null;
    }

    /** 新增收银员，账号已存在时失败 */
    public OpResult add(String account, String password, String name, char sex,
                        int age, String address, String phoneNumber) {
        if (existsAccount(account)) {
            logService.log(LogType.CASHIER, "新增收银员失败", "账号已存在：" + account, false);
            return OpResult.fail("账号已存在：" + account);
        }
        String id = IdGenerator.nextId("C", 4, store.getCashiers(), Cashier::getId);
        store.getCashiers().add(new Cashier(id, account, password, name, sex, age, address, phoneNumber));
        store.saveCashiers();
        logService.log(LogType.CASHIER, "新增收银员", "新增收银员：" + name + "（" + account + "）", true);
        return OpResult.ok("新增收银员成功，编号：" + id);
    }

    /** 根据收银员ID移除 */
    public OpResult removeById(String id) {
        Cashier target = findById(id);
        if (target == null) {
            logService.log(LogType.CASHIER, "移除收银员失败", "收银员不存在：" + id, false);
            return OpResult.fail("收银员不存在：" + id);
        }
        store.getCashiers().remove(target);
        store.saveCashiers();
        logService.log(LogType.CASHIER, "移除收银员", "移除收银员：" + target.getName() + "（" + id + "）", true);
        return OpResult.ok("移除收银员成功：" + target.getName());
    }

    public Cashier findById(String id) {
        return Finder.find(store.getCashiers(), id, Cashier::getId);
    }
}
