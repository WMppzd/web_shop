package com.shop.dao;

import com.shop.domain.Category;
import com.shop.domain.Order;
import com.shop.domain.OrderItems;
import com.shop.domain.Product;
import com.shop.until.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductDao {
    public List<Product> getHotPro() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from product  where is_hot=? order by pdate desc limit ?,?";
        return queryRunner.query(sql,new BeanListHandler<Product>(Product.class),1,0,9);
    }

    public List<Product> getNewPro() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from product order by pdate desc limit ?,?";
        return queryRunner.query(sql,new BeanListHandler<Product>(Product.class),0,9);
    }

    public List<Category> getCategory() throws SQLException {
        QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from category";
        return queryRunner.query(sql,new BeanListHandler<Category>(Category.class));
    }

    public List<Product> getProByCid(String cid, int pageIndex, int pageSize) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from product where cid=? limit ?,?";
        return queryRunner.query(sql,new BeanListHandler<Product>(Product.class),cid,pageIndex,pageSize);
    }

    public int getTotal(String cid) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select count(*) from product where cid=?";
        Long total=(Long) queryRunner.query(sql,new ScalarHandler(),cid);
        return  total.intValue();
    }

    public Product getProInfo(String pid) throws SQLException {
        QueryRunner queryRunner=new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select * from product where pid=?";
       return queryRunner.query(sql,new BeanHandler<Product>(Product.class),pid);
    }

    public void submitOrder(Order order) throws SQLException {
        QueryRunner queryRunner=new QueryRunner();
        String sql="insert into orders values(?,?,?,?,?,?,?,?)";
        Connection connection = DataSourceUtils.getConnection();
        queryRunner.update(connection,sql,order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),
                order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());

    }

    public void addOrderItem(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into orderitem values(?,?,?,?,?)";
        Connection conn = DataSourceUtils.getConnection();
        List<OrderItems> orderItems = order.getOrderItems();
        for(OrderItems item : orderItems){
            runner.update(conn,sql,item.getItemid(),item.getCount(),item.getSubtotal(),item.getProduct().getPid(),item.getOrder().getOid());
        }
    }

    public void paypaymoney(Order order) throws SQLException {
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql="update orders set address=?,name=?,telephone=? where oid=?";
        runner.update(sql, order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
    }
}
