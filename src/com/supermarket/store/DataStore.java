package com.supermarket.store;

import com.supermarket.entity.Cashier;
import com.supermarket.entity.Log;
import com.supermarket.entity.Order;
import com.supermarket.entity.Product;
import com.supermarket.entity.ProductType;
import com.supermarket.enums.LogType;
import com.supermarket.util.CsvUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据仓库：负责内存数据与磁盘文件的持久化。
 * 不使用数据库，数据以可读的 CSV 文本文件保存在项目根目录 data 目录下，
 * 程序关闭后重新打开，数据依然存在。
 */
public final class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    private static final String DIR = "data";
    private static final String FILE_CASHIER = "cashiers.txt";
    private static final String FILE_PRODUCT = "products.txt";
    private static final String FILE_TYPE = "types.txt";
    private static final String FILE_ORDER = "orders.txt";
    private static final String FILE_LOG = "logs.txt";

    /** 日期存储格式 */
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final List<Cashier> cashiers = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();
    private final List<ProductType> types = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Log> logs = new ArrayList<>();

    private DataStore() {
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    /** 从磁盘加载全部数据 */
    public void load() {
        cashiers.clear();
        loadCashiers();
        products.clear();
        loadProducts();
        types.clear();
        loadTypes();
        orders.clear();
        loadOrders();
        logs.clear();
        loadLogs();
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<ProductType> getTypes() {
        return types;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Log> getLogs() {
        return logs;
    }

    /**
     * 是否没有任何业务数据（用于判断首次启动）。
     * 日志每次登录都会新增，不能作为首次启动的判断依据，故不参与判定。
     */
    public boolean isEmpty() {
        return cashiers.isEmpty() && products.isEmpty() && types.isEmpty() && orders.isEmpty();
    }

    // ==================== 保存 ====================

    /** 保存全部数据 */
    public void saveAll() {
        saveCashiers();
        saveProducts();
        saveTypes();
        saveOrders();
        saveLogs();
    }

    public void saveCashiers() {
        List<String> lines = new ArrayList<>();
        lines.add("# id,account,password,name,sex,age,address,phoneNumber");
        for (Cashier cashier : cashiers) {
            lines.add(CsvUtil.toLine(cashier.getId(), cashier.getAccount(), cashier.getPassword(),
                    cashier.getName(), String.valueOf(cashier.getSex()),
                    String.valueOf(cashier.getAge()), cashier.getAddress(), cashier.getPhoneNumber()));
        }
        writeLines(FILE_CASHIER, lines);
    }

    public void saveProducts() {
        List<String> lines = new ArrayList<>();
        lines.add("# id,name,price,count,typeId");
        for (Product product : products) {
            lines.add(CsvUtil.toLine(product.getId(), product.getName(),
                    product.getPrice().toPlainString(), String.valueOf(product.getCount()),
                    product.getTypeId()));
        }
        writeLines(FILE_PRODUCT, lines);
    }

    public void saveTypes() {
        List<String> lines = new ArrayList<>();
        lines.add("# id,name");
        for (ProductType type : types) {
            lines.add(CsvUtil.toLine(type.getId(), type.getName()));
        }
        writeLines(FILE_TYPE, lines);
    }

    public void saveOrders() {
        List<String> lines = new ArrayList<>();
        lines.add("# id,cashierId,totalPrice,date,items(productId:count;...)");
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        for (Order order : orders) {
            StringBuilder items = new StringBuilder();
            for (Map.Entry<String, Integer> entry : order.getProduct().entrySet()) {
                if (items.length() > 0) {
                    items.append(';');
                }
                items.append(entry.getKey()).append(':').append(entry.getValue());
            }
            String date = order.getDate() == null ? "" : format.format(order.getDate());
            lines.add(CsvUtil.toLine(order.getId(), order.getCashierId(),
                    order.getTotalPrice().toPlainString(), date, items.toString()));
        }
        writeLines(FILE_ORDER, lines);
    }

    public void saveLogs() {
        List<String> lines = new ArrayList<>();
        lines.add("# id,title,description,date,success,logType");
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        for (Log log : logs) {
            String date = log.getDate() == null ? "" : format.format(log.getDate());
            String type = log.getLogType() == null ? "" : log.getLogType().name();
            lines.add(CsvUtil.toLine(log.getId(), log.getTitle(), log.getDescription(),
                    date, String.valueOf(log.isSuccess()), type));
        }
        writeLines(FILE_LOG, lines);
    }

    // ==================== 加载 ====================

    private void loadCashiers() {
        for (String[] f : readRows(FILE_CASHIER)) {
            if (f.length < 8) {
                continue;
            }
            try {
                char sex = f[4].isEmpty() ? ' ' : f[4].charAt(0);
                cashiers.add(new Cashier(f[0], f[1], f[2], f[3], sex,
                        Integer.parseInt(f[5].trim()), f[6], f[7]));
            } catch (RuntimeException e) {
                warn("收银员", f);
            }
        }
    }

    private void loadProducts() {
        for (String[] f : readRows(FILE_PRODUCT)) {
            if (f.length < 5) {
                continue;
            }
            try {
                products.add(new Product(f[0], f[1], new BigDecimal(f[2].trim()),
                        Integer.parseInt(f[3].trim()), f[4]));
            } catch (RuntimeException e) {
                warn("商品", f);
            }
        }
    }

    private void loadTypes() {
        for (String[] f : readRows(FILE_TYPE)) {
            if (f.length < 2) {
                continue;
            }
            types.add(new ProductType(f[0], f[1]));
        }
    }

    private void loadOrders() {
        for (String[] f : readRows(FILE_ORDER)) {
            if (f.length < 5) {
                continue;
            }
            try {
                Map<String, Integer> items = new LinkedHashMap<>();
                if (!f[4].isBlank()) {
                    for (String pair : f[4].split(";")) {
                        String[] kv = pair.split(":");
                        if (kv.length == 2) {
                            items.put(kv[0], Integer.parseInt(kv[1].trim()));
                        }
                    }
                }
                Date date = f[3].isBlank() ? new Date() : new SimpleDateFormat(DATE_PATTERN).parse(f[3]);
                orders.add(new Order(f[0], f[1], items, new BigDecimal(f[2].trim()), date));
            } catch (RuntimeException | ParseException e) {
                warn("订单", f);
            }
        }
    }

    private void loadLogs() {
        for (String[] f : readRows(FILE_LOG)) {
            if (f.length < 6) {
                continue;
            }
            try {
                Date date = f[3].isBlank() ? new Date() : new SimpleDateFormat(DATE_PATTERN).parse(f[3]);
                LogType logType = f[5].isBlank() ? null : LogType.valueOf(f[5]);
                logs.add(new Log(f[0], f[1], f[2], date, Boolean.parseBoolean(f[4]), logType));
            } catch (RuntimeException | ParseException e) {
                warn("日志", f);
            }
        }
    }

    // ==================== 文件读写 ====================

    /** 读取文件全部数据行（跳过空行与以 # 开头的注释行） */
    private List<String[]> readRows(String fileName) {
        List<String[]> rows = new ArrayList<>();
        File file = new File(DIR, fileName);
        if (!file.exists()) {
            return rows;
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                rows.add(CsvUtil.parse(line).toArray(new String[0]));
            }
        } catch (IOException e) {
            System.err.println("读取数据文件失败：" + fileName + "（" + e.getMessage() + "）");
        }
        return rows;
    }

    /** 覆盖写入数据行（UTF-8 文本） */
    private void writeLines(String fileName, List<String> lines) {
        File dir = new File(DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("无法创建数据目录：" + dir.getAbsolutePath());
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(new File(dir, fileName)), StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("保存数据文件失败：" + fileName + "（" + e.getMessage() + "）");
        }
    }

    private void warn(String entity, String[] fields) {
        System.err.println("跳过格式错误的数据行（" + entity + "）：" + String.join(",", fields));
    }
}
