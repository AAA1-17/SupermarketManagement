package com.supermarket.entity;

/**
 * 收银员实体
 */
public class Cashier {

    /** 收银员ID（系统自动生成） */
    private final String id;
    /** 账号 */
    private final String account;
    /** 密码 */
    private final String password;
    /** 姓名 */
    private final String name;
    /** 性别 */
    private final char sex;
    /** 年龄 */
    private final int age;
    /** 家庭地址 */
    private final String address;
    /** 手机号 */
    private final String phoneNumber;

    public Cashier(String id, String account, String password, String name,
                   char sex, int age, String address, String phoneNumber) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public char getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("%-6s %-10s %-8s %-3s %-4d %-13s %-10s %s",
                id, account, name, sex, age, phoneNumber, address, password);
    }
}
