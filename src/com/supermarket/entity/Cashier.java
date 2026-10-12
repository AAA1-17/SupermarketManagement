package com.supermarket.entity;

import java.util.ArrayList;
import java.util.List;

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
        return String.format("%-6s %-12s %-10s %-3s %-4d %-10s %-13s %s",
                id, account, password, sex, age, name, phoneNumber, address);
    }

    // ==================== 测试用例 ====================

    /** 直接运行本方法可单独测试收银员实体 */
    public static void main(String[] args) {
        List<Cashier> cashiers = new ArrayList<>();
        cashiers.add(new Cashier("C0001", "92930120027", "p4i5DR86", "端木玉", '女', 32,
                "河南省平顶山市鲁山县原平乡田土村民长组50号", "13290023466"));
        cashiers.add(new Cashier("C0002", "46249851193", "35wL59Cs", "水永景", '女', 34,
                "黑龙江省大庆市龙凤区星采中路34号塞纳新天地1栋1单元1704房", "16215216549"));
        cashiers.add(new Cashier("C0003", "83829839551", "7218szUB", "路影", '女', 64,
                "山东省威海市环翠区胜丰里西路438号田园汇8栋1单元0801房", "16902239467"));
        cashiers.add(new Cashier("C0004", "15366753375", "920Rt0w8", "司空波修", '女', 15,
                "青海省果洛藏族自治州甘德县兰天乡八班村泵湘组83号", "18639911577"));
        cashiers.add(new Cashier("C0005", "65923562653", "bE5qK922", "桑葳", '男', 37,
                "陕西省汉中市勉县乐林镇汉村永九组26号", "15784143653"));

        System.out.println("------------------- 收银员列表 -------------------");
        System.out.printf("%-6s %-12s %-10s %-3s %-4s %-10s %-13s %s%n",
                "ID", "账号", "密码", "性别", "年龄", "姓名", "联系方式", "地址");
        for (Cashier cashier : cashiers) {
            System.out.println(cashier);
        }
        System.out.println("------------------- 收银员列表 -------------------");
        System.out.println("共计" + cashiers.size() + "条数据。");

        // 顺带做点小统计，验证 getter 可用
        int totalAge = 0;
        int femaleCount = 0;
        for (Cashier cashier : cashiers) {
            totalAge += cashier.getAge();
            if (cashier.getSex() == '女') {
                femaleCount++;
            }
        }
        System.out.println("平均年龄：" + (totalAge / cashiers.size())
                + "岁，女收银员：" + femaleCount + " 人，男收银员：" + (cashiers.size() - femaleCount) + " 人。");
    }
}
