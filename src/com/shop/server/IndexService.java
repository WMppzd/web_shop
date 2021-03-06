package com.shop.server;

import com.shop.dao.ProductDao;
import com.shop.domain.BeanProByCid;
import com.shop.domain.Category;
import com.shop.domain.Order;
import com.shop.domain.Product;
import com.shop.until.DataSourceUtils;

import javax.xml.crypto.Data;
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

    public BeanProByCid<Product> getProBycid(String cid,int currentPage,int currentCount) {
        int totalCount=0;
        int totalPage=0;
        int pageIndex=0;
        int pageSize=0;
        List<Product> pro=null;
        BeanProByCid<Product> proList = new BeanProByCid<Product>();
        ProductDao productDao=new ProductDao();
        pageIndex=(currentPage-1)*currentCount;
        pageSize=currentCount;
        try {
            pro= productDao.getProByCid(cid,pageIndex,pageSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            totalCount=productDao.getTotal(cid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        proList.setTotalCount(totalCount);
        totalPage= (int) Math.ceil(totalCount*1.0/currentCount);
        proList.setTotalPage(totalPage);
        proList.setList(pro);
        proList.setCurrentCount(currentCount);
        proList.setCurrentPage(currentPage);
        return  proList;
    }

    public Product getProInfo(String pid) {
        ProductDao productDao=new ProductDao();
        Product product= null;
        try {
            product = productDao.getProInfo(pid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void submitOrder(Order order) {
        ProductDao dao=new ProductDao();
        try {
            DataSourceUtils.startTransaction();
            dao.submitOrder(order);
            dao.addOrderItem(order);
        } catch (SQLException e) {
            try {
                DataSourceUtils.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally{
            try {
                DataSourceUtils.commitAndRelease();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void paypaymoney(Order order)  {
        ProductDao dao=new ProductDao();
        try {
            dao.paypaymoney(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
