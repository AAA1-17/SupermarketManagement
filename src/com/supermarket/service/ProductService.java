package com.supermarket.service;

import com.supermarket.entity.Product;
import com.supermarket.entity.ProductType;
import com.supermarket.enums.LogType;
import com.supermarket.util.Finder;
import com.supermarket.util.IdGenerator;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品管理：商品与商品类别的增删改查、入库出库。
 */
public class ProductService extends BaseService {

    // ==================== 商品 ====================

    public List<Product> listProducts() {
        return store.getProducts();
    }

    public Product findProductById(String id) {
        return Finder.find(store.getProducts(), id, Product::getId);
    }

    /**
     * 新增商品：若同名商品已存在，则只累加数量。
     */
    public OpResult addProduct(String name, BigDecimal price, int count, String typeId) {
        Product exist = Finder.find(store.getProducts(), name, Product::getName);
        if (exist != null) {
            exist.setCount(exist.getCount() + count);
            store.saveProducts();
            logService.log(LogType.PRODUCT, "新增商品",
                    "商品已存在，数量累加：" + name + "，现有数量：" + exist.getCount(), true);
            return OpResult.ok("商品【" + name + "】已存在，已累加数量，现有数量：" + exist.getCount());
        }
        String id = IdGenerator.nextId("P", 4, store.getProducts(), Product::getId);
        store.getProducts().add(new Product(id, name, price, count, typeId));
        store.saveProducts();
        logService.log(LogType.PRODUCT, "新增商品", "新增商品：" + name + "（" + id + "）", true);
        return OpResult.ok("新增商品成功，商品编号：" + id);
    }

    public OpResult removeProduct(String id) {
        Product target = findProductById(id);
        if (target == null) {
            logService.log(LogType.PRODUCT, "移除商品失败", "商品不存在：" + id, false);
            return OpResult.fail("商品不存在：" + id);
        }
        store.getProducts().remove(target);
        store.saveProducts();
        logService.log(LogType.PRODUCT, "移除商品", "移除商品：" + target.getName() + "（" + id + "）", true);
        return OpResult.ok("移除商品成功：" + target.getName());
    }

    /** 修改商品价格 */
    public OpResult updatePrice(String id, BigDecimal newPrice) {
        Product target = findProductById(id);
        if (target == null) {
            logService.log(LogType.PRODUCT, "修改商品价格失败", "商品不存在：" + id, false);
            return OpResult.fail("商品不存在：" + id);
        }
        BigDecimal old = target.getPrice();
        target.setPrice(newPrice);
        store.saveProducts();
        logService.log(LogType.PRODUCT, "修改商品价格",
                "商品：" + target.getName() + " 价格由 " + old + " 修改为 " + newPrice, true);
        return OpResult.ok("修改成功，商品【" + target.getName() + "】价格由 " + old + " 变为 " + newPrice);
    }

    /** 商品入库 */
    public OpResult stockIn(String id, int quantity) {
        Product target = findProductById(id);
        if (target == null) {
            logService.log(LogType.PRODUCT, "商品入库失败", "商品不存在：" + id, false);
            return OpResult.fail("商品不存在：" + id);
        }
        target.setCount(target.getCount() + quantity);
        store.saveProducts();
        logService.log(LogType.PRODUCT, "商品入库",
                "商品：" + target.getName() + " 入库 " + quantity + "，现有数量：" + target.getCount(), true);
        return OpResult.ok("入库成功，商品【" + target.getName() + "】现有数量：" + target.getCount());
    }

    /** 商品出库 */
    public OpResult stockOut(String id, int quantity) {
        Product target = findProductById(id);
        if (target == null) {
            logService.log(LogType.PRODUCT, "商品出库失败", "商品不存在：" + id, false);
            return OpResult.fail("商品不存在：" + id);
        }
        if (target.getCount() < quantity) {
            logService.log(LogType.PRODUCT, "商品出库失败",
                    "库存不足：" + target.getName() + "，现有 " + target.getCount(), false);
            return OpResult.fail("库存不足，商品【" + target.getName() + "】现有数量：" + target.getCount());
        }
        target.setCount(target.getCount() - quantity);
        store.saveProducts();
        logService.log(LogType.PRODUCT, "商品出库",
                "商品：" + target.getName() + " 出库 " + quantity + "，现有数量：" + target.getCount(), true);
        return OpResult.ok("出库成功，商品【" + target.getName() + "】现有数量：" + target.getCount());
    }

    // ==================== 商品类别 ====================

    public List<ProductType> listTypes() {
        return store.getTypes();
    }

    /** 新增商品类别，名称不能重复 */
    public OpResult addType(String name) {
        if (Finder.find(store.getTypes(), name, ProductType::getName) != null) {
            logService.log(LogType.PRODUCT_TYPE, "新增商品类别失败", "类别名称已存在：" + name, false);
            return OpResult.fail("该商品类别已存在，不能重复添加：" + name);
        }
        String id = IdGenerator.nextId("T", 4, store.getTypes(), ProductType::getId);
        store.getTypes().add(new ProductType(id, name));
        store.saveTypes();
        logService.log(LogType.PRODUCT_TYPE, "新增商品类别", "新增类别：" + name + "（" + id + "）", true);
        return OpResult.ok("新增商品类别成功，类别编号：" + id);
    }

    public OpResult removeType(String id) {
        ProductType target = Finder.find(store.getTypes(), id, ProductType::getId);
        if (target == null) {
            logService.log(LogType.PRODUCT_TYPE, "移除商品类别失败", "类别不存在：" + id, false);
            return OpResult.fail("商品类别不存在：" + id);
        }
        store.getTypes().remove(target);
        store.saveTypes();
        logService.log(LogType.PRODUCT_TYPE, "移除商品类别", "移除类别：" + target.getName() + "（" + id + "）", true);
        return OpResult.ok("移除商品类别成功：" + target.getName());
    }

    // ==================== 库存统计 ====================

    /** 库存商品总数量 */
    public int totalStockCount() {
        int sum = 0;
        for (Product product : store.getProducts()) {
            sum += product.getCount();
        }
        return sum;
    }

    /** 库存商品总金额 */
    public BigDecimal totalStockAmount() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Product product : store.getProducts()) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(product.getCount())));
        }
        return sum;
    }
}
