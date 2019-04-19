package com.shop.server;

import com.shop.dao.ProductDao;
import com.shop.domain.Category;
import com.shop.domain.Product;

import java.sql.SQLException;
import java.util.List;

public class IndexService {
    public List<Product> findNewPro() {
        ProductDao productDao=new ProductDao();
        List<Product> products = null;
        try {
            products= productDao.getNewPro();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> findHotPro() {
        ProductDao productDao=new ProductDao();
        List<Product> products = null;
        try {
            products= productDao.getHotPro();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Category> getCategory() {
        ProductDao productDao=new ProductDao();
        List<Category> categories= null;
        try {
            categories = productDao.getCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
