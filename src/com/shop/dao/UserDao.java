package com.shop.dao;

import com.shop.domain.User;
import com.shop.until.DataSourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

public class UserDao {
    public int register(User user) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql = "insert into user values (?,?,?,?,?,?,?,?,?,?)";

        int num=queryRunner.update(sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName(), user.getEmail(), user.getTelephone(), user.getBirthday(), user.getSex(), user.getState(), user.getCode());
            System.out.println(num);
        return  num;
    }

    public int active(String code) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql="update user set state=? where code=?";
        return queryRunner.update(sql,"1",code);
    }

    public int isHaveName(String username) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
        String sql="select count(*) from user where username=?";

        return((Long) queryRunner.query(sql,new ScalarHandler(),username)).intValue();
    }
}
