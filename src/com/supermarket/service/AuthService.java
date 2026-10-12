package com.supermarket.service;

import com.supermarket.entity.Cashier;
import com.supermarket.enums.LogType;
import com.supermarket.util.Finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 登录鉴权服务：
 * 1. 系统登录：从 db.properties 配置文件读取用户名与密码进行校验；
 * 2. 收银员登录：校验收银员的账号与密码。
 */
public class AuthService extends BaseService {

    private static final String CONFIG_FILE = "db.properties";
    private static final String KEY_USERNAME = "supermarket.username";
    private static final String KEY_PASSWORD = "supermarket.password";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123456";

    private final String username;
    private final String password;

    public AuthService() {
        Properties properties = loadConfig();
        username = properties.getProperty(KEY_USERNAME, DEFAULT_USERNAME);
        password = properties.getProperty(KEY_PASSWORD, DEFAULT_PASSWORD);
    }

    /** 从配置文件加载用户信息，若不存在则生成默认配置文件 */
    private Properties loadConfig() {
        File file = new File(CONFIG_FILE);
        Properties properties = new Properties();
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                properties.load(in);
            } catch (IOException e) {
                System.err.println("读取配置文件失败：" + e.getMessage());
            }
        } else {
            createDefaultConfig(file);
        }
        return properties;
    }

    private void createDefaultConfig(File file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            Properties properties = new Properties();
            properties.setProperty(KEY_USERNAME, DEFAULT_USERNAME);
            properties.setProperty(KEY_PASSWORD, DEFAULT_PASSWORD);
            properties.store(out, "Supermarket management system login config");
            System.out.println("未找到配置文件，已生成默认配置 " + file.getAbsolutePath()
                    + "（用户名：" + DEFAULT_USERNAME + " 密码：" + DEFAULT_PASSWORD + "）");
        } catch (IOException e) {
            System.err.println("生成默认配置文件失败：" + e.getMessage());
        }
    }

    /** 系统登录：用户名密码与配置一致则成功 */
    public boolean login(String inputUsername, String inputPassword) {
        boolean success = username.equals(inputUsername) && password.equals(inputPassword);
        logService.log(LogType.LOGIN, success ? "系统登录成功" : "系统登录失败",
                "用户名：" + inputUsername, success);
        return success;
    }

    /** 收银员登录：校验收银员账号与密码 */
    public Cashier cashierLogin(String account, String inputPassword) {
        Cashier cashier = Finder.find(store.getCashiers(), account, Cashier::getAccount);
        if (cashier != null && cashier.getPassword().equals(inputPassword)) {
            logService.log(LogType.LOGIN, "收银员登录成功",
                    "收银员：" + cashier.getName() + "（" + account + "）", true);
            return cashier;
        }
        logService.log(LogType.LOGIN, "收银员登录失败", "账号：" + account, false);
        return null;
    }
}
