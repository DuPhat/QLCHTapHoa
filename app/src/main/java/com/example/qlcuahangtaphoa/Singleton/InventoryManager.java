package com.example.qlcuahangtaphoa.Singleton;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private static volatile InventoryManager instance;
    private List<Product> products;

    private InventoryManager() {
        products = new ArrayList<>();
        // Khởi tạo dữ liệu hoặc tài nguyên khác ở đây.
    }

    public static synchronized InventoryManager getInstance() {
        if (instance == null) {
            synchronized (InventoryManager.class) {
                if (instance == null) {
                    instance = new InventoryManager();
                }
            }
        }
        return instance;
    }

    public void addProduct(Product product) {
        products.add(product);
        // Các thao tác khác liên quan đến việc thêm sản phẩm vào kho.
    }

    public List<Product> getProducts() {
        return products;
    }
}
