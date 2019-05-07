package com.shop.server;

import com.shop.dao.UserDao;
import com.shop.domain.User;

import java.sql.SQLException;

public class UserService {
    public Boolean register(User user)  {
        UserDao userDao=new UserDao();
        int num=0;
        try {
            num=userDao.register(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num>0?true:false;
    }

    public Boolean active(String code) {
        int num=0;
        UserDao userDao=new UserDao();
        try {
            num=userDao.active(code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num>0?true:false;
    }

    public Boolean haveName(String username) {
        int num=0;
        UserDao userDao=new UserDao();
        try {
            num=userDao.isHaveName(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num>0?false:true;
    }

    public User login(String username, String password) throws SQLException {
        UserDao dao=new UserDao();
        return dao.login(username,password);
    }
}
